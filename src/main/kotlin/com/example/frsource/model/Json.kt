package com.example.frsource.model


class Json() : HashMap<String, Any>() {

    init {
        this[KEY_OPER] = DEFAULT_OPER_VALE
        this[KEY_SUCC] = true
        this[KEY_CODE] = DEFAULT_SUCC_CODE
        this[KEY_MESG] = DEFAULT_SUCC_CODE
    }

    constructor(oper: String) : this() {
        this[KEY_OPER] = oper
    }

    constructor(operation: String, success: Boolean, code: Int, mesg: String, data: Any?) : this() {
        this[KEY_OPER] = operation
        this[KEY_SUCC] = success
        this[KEY_CODE] = code
        this[KEY_MESG] = mesg
        if (data != null)
            this[KEY_DATA] = data
    }

    /**
     * 设置操作名称
     */
    public fun oper(operation: String): Json {
        this[KEY_OPER] = operation
        return this
    }

    /**
     * 设置操作结果是否成功标记
     */
    public fun succ(success: Boolean): Json {
        this[KEY_SUCC] = success
        return this
    }

    /**
     * 设置操作结果代码
     */
    public fun code(code: Int): Json {
        this[KEY_CODE] = code
        return this
    }

    /**
     * 设置操作结果信息
     */
    public fun msg(message: String): Json {
        this[KEY_MESG] = message
        return this
    }

    /**
     * 设置操作返回的数据
     */
    public fun data(data: Any): Json {
        this[KEY_DATA] = data
        return this
    }

    /**
     * 设置操作返回的数据, 数据使用自定义的键
     */
    public fun data(key: String, data: Any): Json {
        this[key] = data
        return this
    }

    companion object {
        const val KEY_OPER = "oper"
        const val KEY_SUCC = "succ"
        const val KEY_CODE = "code"
        const val KEY_MESG = "mesg"
        const val KEY_DATA = "data"

        const val DEFAULT_OPER_VALE = "default"
        const val DEFAULT_SUCC_CODE = 200
        const val DEFAULT_FAIL_CODE = 500
        const val DEFAULT_SUCC_MESG = "ok"
        const val DEFAULT_FAIL_MESG = "fail"

        public fun succ(operation: String = DEFAULT_OPER_VALE,
                        code: Int = DEFAULT_SUCC_CODE,
                        message: String = DEFAULT_SUCC_MESG,
                        data: Any? = null): Json {
            return Json(operation, true, code, message, data)
        }

        public fun fail(operation: String = DEFAULT_OPER_VALE,
                        code: Int = DEFAULT_FAIL_CODE,
                        message: String = DEFAULT_FAIL_MESG,
                        data: Any? = null): Json {
            return Json(operation, false, code, message, data)
        }

        public fun result(operation: String,
                          success: Boolean,
                          data: Any? = null): Json {
            return Json(operation,
                    success,
                    if (success) DEFAULT_SUCC_CODE else DEFAULT_FAIL_CODE,
                    if (success) DEFAULT_SUCC_MESG else DEFAULT_FAIL_MESG,
                    data)
        }
    }
}