package org.carbon.crawler.admin.extend.carbon.validation

import org.carbon.objects.validation.BeCounterExpression
import org.carbon.objects.validation.evaluation.Evaluation
import org.carbon.objects.validation.evaluation.source.Code
import org.carbon.objects.validation.evaluation.source.ParamList
import org.carbon.objects.validation.matcher.max
import org.carbon.objects.validation.matcher.min
import org.carbon.objects.validation.matcher.reject


val Min: (i: Int) -> BeCounterExpression<Int> = { i -> { this min i } }
val Max: (i: Int) -> BeCounterExpression<Int> = { i -> { this max i } }
val NumberString: BeCounterExpression<String> = { this.isNumberString() }

fun String.isNumberString(): Evaluation = this.toIntOrNull()
        ?.let { Evaluation.Accepted }
        ?: this.reject(
                Code("String.NumberString"),
                ParamList(emptyList<String>()),
                "Not a number"
        )

