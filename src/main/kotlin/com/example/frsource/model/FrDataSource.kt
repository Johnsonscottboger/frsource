package com.example.frsource.model

/**
 * 帆软数据源
 */
data class FrDataSource(
        /**
         * 数据源名称
         */
        val name: String,

        /**
         * 数据源指定的类名称
         */
        val typeName: String,

        /**
         * 数据源查询文本
         */
        val queryText: String,

        /**
         * 数据源中的列
         */
        val columns: List<FrDataSourceColumn> = listOf()) {
}