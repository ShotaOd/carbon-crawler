package org.carbon.crawler.model.extend.composer

import org.carbon.composer.Composable
import org.carbon.crawler.model.extend.exposed.transactionL
import org.carbon.crawler.model.infra.meta.Meta
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class Transaction<T>(private val logging: Boolean = false) : Composable<T>() {
    override fun invoke(): T =
        if (logging) transactionL { super.callChild() }
        else transaction { super.callChild() }
}

interface DBUtil {
    fun clean()
}

object RollbackTransaction : Composable<Unit>() {
    private object RollbackException : Exception("for Rollback purpose")
    private class DBUtilImpl : DBUtil {
        override fun clean() {
            SchemaUtils.create(*Meta.tablesByAncestor)
            Meta.tablesByAncestor
                .reversed()
                .forEach { it.deleteAll() }
        }
    }

    private val log = LoggerFactory.getLogger(RollbackTransaction::class.java)
    override fun invoke() {
        context.setAs(DBUtilImpl(), DBUtil::class)
        try {
            transactionL {
                super.callChild()
                throw RollbackException
            }
        } catch (ex: RollbackException) {
            log.info("rollback! catch rollback-purpose exception")
        }
    }
}