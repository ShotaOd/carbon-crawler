package org.carbon.crawler.stream.flow.crawlTarget

import org.carbon.crawler.stream.core.config.DataConfig
import org.carbon.crawler.stream.core.config.TriggerConfig
import org.carbon.crawler.stream.core.extend.spring.cloud.SourceConfigSupport
import org.carbon.crawler.stream.message.crawlTarget.CrawlTargetPayload
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.integration.scheduling.PollerMetadata

/**
 * @author Soda 2018/08/13.
 */
@Import(DataConfig::class, TriggerConfig::class)
@EnableBinding(Source::class)
@Configuration
class CrawlTargetSourceConfig(
        poller: PollerMetadata,
        source: Source
) : SourceConfigSupport<CrawlTargetPayload>(
        poller,
        source,
        ::crawlTargetSource
)