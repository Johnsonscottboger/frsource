package com.example.frsource.controller

import com.example.frsource.model.Json
import com.example.frsource.service.IFrSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File

@Controller
class HomeController {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var frSource: IFrSource

    /**
     * 帆软根路径
     */
    @Value("\${config.frRootPath}")
    private lateinit var frRootPath: String

    @GetMapping("/")
    fun index(): String {
        log.info("Application Running...")
        return "index"
    }

    /**
     * 获取根目录的子级
     */
    @ResponseBody
    @GetMapping("subs")
    fun subDictionaries(): Json {
        val root = File(this.frRootPath)
        val subs = root.listFiles { p -> p.isDirectory }!!.map { p ->p.name }
        return Json.succ("获取子级", data = subs)
    }

    /**
     * 查询
     */
    @ResponseBody
    @GetMapping("query")
    fun query(@RequestParam("subDir") subDir: String, @RequestParam("keywords") keywords: String): Json {
        val operation = "查询"
        return try {
            val result = this.frSource.query(subDir, keywords)
            Json.succ(operation, data = result)
        } catch (ex: Exception) {
            Json.fail(operation, message = ex.message!!)
        }
    }
}