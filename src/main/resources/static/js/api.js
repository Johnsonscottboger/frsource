/*
定义 API 接口
 */

let request = axios;
let api = {

    /**
     * 获取子级
     */
    getSubDirs : function(){
      return request.get('/subs');
    },

    /**
     * 查询
     * @param subDir 子级
     * @param keywords 关键字
     * @returns {*}
     */
    query: function (subDir, keywords) {
        if (keywords == null || keywords === 'null' || keywords === undefined || keywords === 'undefined' || keywords.match(/^[\s]*$/)) {
            return
        } else {
            return request.get('/query?subDir=' + subDir + '&keywords=' + keywords);
        }
    }
}