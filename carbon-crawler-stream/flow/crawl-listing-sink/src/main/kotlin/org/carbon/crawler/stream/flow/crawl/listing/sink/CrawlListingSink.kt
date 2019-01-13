package org.carbon.crawler.stream.flow.crawl.listing.sink

import org.carbon.crawler.model.domain.HostRepository
import org.carbon.crawler.model.extend.exposed.batchInsertV2
import org.carbon.crawler.model.infra.record.HostTable
import org.carbon.crawler.model.infra.record.PageAttributeTable
import org.carbon.crawler.model.infra.record.PageTable
import org.carbon.crawler.stream.core.extend.selenium.DriverFactory
import org.carbon.crawler.stream.core.extend.selenium.toBy
import org.carbon.crawler.stream.core.extend.spring.cloud.TypedSink
import org.carbon.crawler.stream.message.crawlOrder.ListingOrderPayload
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import kotlin.properties.Delegates

private val log: Logger = LoggerFactory.getLogger("crawler")
fun crawlListingSink(driverFactory: DriverFactory): TypedSink<ListingOrderPayload> = { (hostId) ->
    HostRepository.fetchBy(hostId)
        ?.let { entity ->
            driverFactory.use { driver ->
                // ______________________________________________________
                //
                // @ in listing page
                driver.get(entity.listingUrl)
                val detailPathEls = driver.findElements(entity.listingQuery.toBy())
                val data = detailPathEls
                    .map {
                        // ______________________________________________________
                        //
                        // @ in detail page
                        val href = it.getAttribute("href")
                        log.info("[{}], findATag: @href: {}", entity.listingUrl, href)
                        href
                    }.map { href ->
                        driver.get(href)
                        val title = driver.title
                        val attributes = entity.detailQueries
                            .map { detail ->
                                val query = detail.query.toBy()
                                val value = driver.findElement(query).text
                                log.info("[{}], queryAttribute @xpath: {} @text: {}", entity.listingUrl, query, value)
                                PageAttributeStatement(detail.queryName, value)
                            }
                        PageStatement(
                            hostId,
                            title,
                            href,
                            attributes
                        )
                    }

                // ______________________________________________________
                //
                // @ batch inserts
                // - pages
                // - attributes
                val now = LocalDateTime.now()
                val pageIds = PageTable.batchInsertV2(data,
                    onDuplicate = {
                        this[PageTable.title] = it.title
                        this[PageTable.updatedAt] = now
                    },
                    body = {
                        it.applyInsert(this)
                    })
                // attributes
                val attributeData = data.withIndex()
                    .flatMap { (index, value) ->
                        val pageId = pageIds[index]
                        value.attributes.map { it.withPageId(pageId) }
                    }
                PageAttributeTable.batchInsertV2(attributeData,
                    onDuplicate = {
                        this[PageAttributeTable.value] = it.value
                        this[PageAttributeTable.updatedAt] = now
                    },
                    body = {
                        it.applyInsert(this)
                    })
            }
        }
}


private data class PageAttributeStatement(
    val key: String,
    val value: String
) {
    private var pageId by Delegates.notNull<Long>()
    fun withPageId(pageId: Long): PageAttributeStatement {
        this.pageId = pageId
        return this
    }

    fun applyInsert(r: BatchInsertStatement) {
        r[PageAttributeTable.pageId] = EntityID(pageId, PageTable)
        r[PageAttributeTable.key] = key
        r[PageAttributeTable.value] = value
        r[PageAttributeTable.type] = "text"
    }
}

private data class PageStatement(
    val hostId: Long,
    val title: String,
    val url: String,
    val attributes: List<PageAttributeStatement>
) {
    fun applyInsert(r: BatchInsertStatement) {
        r[PageTable.hostId] = EntityID(hostId, HostTable)
        r[PageTable.title] = title
        r[PageTable.url] = url
    }
}