var easyconf = new Object({
    // basic definition here, pls do not modify them 
    names:{
        SEARCH:"SEARCH",
        INSERT:"INSERT",
        DETAIL:"DETAIL",
        UPDATE:"UPDATE",
        DELETE:"DELETE",
        FRESH:"FRESH",
        PREV:"PREV",
        NEXT:"NEXT",
        GO:"GO",
        RETURN:"RETURN",
        COMMIT:"COMMIT"
    },
    subTitles:['LIST', 'DETAIL', 'UPDATE', 'INSERT'],
    msgs:{
        OK:"OK",
        NOTNULL:"NOT NULL",
        INTEGER:"MUST INPUT INTEGER",
        DOUBLE:"MUST INPUT NUMBER",
        TYPEERROR:"TYPE ERROR",
        DATE:"MUST INPUT DATE:[YYYY-MM-DD hh:mm:ss.ms]",
        NOTUNIQUE:"NOT UNIQUE",
    },
    controls:{
        TEXT:0,
        RADIO:1,
        CBOX:2
    },
    views:{
        LIST:0,
        DETAIL:1,
        UPDATE:2,
        INSERT:3
    },
    candidates:
    {
        FIXED:0,
        FLEXIBLE:1
    },
    apps:{
        INIT:"init",
        LIST:"list",
        DETAIL:"detail",
        DELETE:"delete",
        UPDATE:"update",
        INSERT:"insert",
        RANGE:"range"
    },
    dateRE:RegExp("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\\d|3[0-1]) ([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d\\.\\d{2}$"),
    sep:"|",

    // basic function here, pls do not modify them
    init:function(http, table) {
        this.table = table;

        this.listContent = [];
        this.detailContent = {};
        this.primaryKeyContent = {};
        this.candidate = {};
        this.range = {};
        this.err = {};
        this.errmsg = {};
        this.check = {};
        this.view = null;
        this.cursor = 0;
        this.queryUrl = null;
        this.view = this.views.LIST;

        url = this.formatInitUrl(table);
        url = "./init.json";
        easyconf = this;
        http.get(url).success(
            function(response) {
                easyconf.conf = response;
                easyconf.setCandidateAndRange();
                easyconf.isInit = true;
            }
        );
    },

    search:function(http, query) {
        console.log("search...");
        data = this.getShowCol();
        url = this.formatListHomeUrl(data, query);
        url = "./list.json";
        console.log("[url]" + url);
        easyconf = this;
        this.lock();
        http.get(url).success(
            function(response) {
                easyconf.setListContent(response.data);
                easyconf.unLock();
            }
        );
    },

    insert:function() {
        this.detailContent = {};
        this.keyContent = {};
        this.view = 3; // INSERT
        this.allCheck();
    },

    detail:function(http, row, view) {
        query = this.getPKV(row);
        url = this.formatDetailUrl(query);
        url = "./detail.json"
        console.log("[url]" + url);
        easyconf = this;
        this.lock();
        http.get(url).success(
            function(response) {
                easyconf.setDetailContent(response.data);
                easyconf.view = view; 
                if (view == easyconf.views.UPDATE) {
                    easyconf.allCheck();
                }
                easyconf.unLock();
            }
        );
    },

    dlt:function(http, row) {
        console.log("delete...");
        query = this.getPKV(row);
        url = this.formatDeleteUrl(query);
        url = "./OK.json"
        console.log("[url]" + url);
        easyconf = this;
        this.lock();
        //http.post(url).success(
        http.get(url).success(
            function(response) {
                easyconf.freshCall();
                easyconf.unLock();
            }
        );
    },

    jump:function(http) {
        url = this.formatListNewUrl()
        url = "./list.json";
        console.log("[url]" + url);
        easyconf = this;
        this.lock();
        http.get(url).success(
            function(response) {
                easyconf.setListContent(response.data);
                easyconf.unLock();
            }
        );
    },

    prev:function(http) {
        console.log("prev...");
        this.setPrevCursor();
        this.jump(http);
    },

    next:function(http) {
        console.log("next...");
        this.setNextCursor();
        this.jump(http);
    },

    go:function(http, page) {
        console.log("go...");
        this.setGoCursor(page);
        this.jump(http);
    },

    fresh:function(http) {
        console.log("fresh...");
        if (this.isSearched()) {
            this.jump(http);
        }
    },

    change:function(column) {
        //should be the first check
        if(column.isPrimaryKey && !this.checkUnique()) {
            return false;
        }
        if (this.isBlank(column)) {
            if (!this.checkNull(column)) {
                return false;
            }
        }
        else {
            if(!this.checkType(column)) {
                return false;
            }

            if(!this.selfCheck(column)) {
                return false;
            }

        }
        this.setOK(column);
        return true;
    },

    setCheck:function(column, change) {
        this.check[column.id] = change;
    },

    focus:function(http, column) {
        if (this.isColFlexible(column)) {
            url = this.formatRangeUrl(column);
            url = "./range.json"
            console.log("[url]" + url);
            easyconf = this;
            this.lock();
            http.get(url).success(
                function(response) {
                    easyconf.range[column.id] = response.data;
                    easyconf.unLock();
                }
            );
        }
    },

    back:function() {
        this.view = this.views.LIST; //LIST
        this.freshCall();
    },

    commit:function(http) {
        query = this.getDetailPKV();
        if (this.isUpdateView()) {
            url = this.formatUpdateUrl(query);
        }
        else if (this.isInsertView()) {
            url = this.formatInsertUrl();
        }
        //url = "./OK.json"
        url = "./NU.json"
        console.log("[url]" + url);
        easyconf = this;
        this.lock();
        //http.post(url).success(
        http.get(url).success(
            function(response) {
                if (response.result == "00") {
                    if (easyconf.isInsertView()) {
                        easyconf.detailContent = {};
                        easyconf.keyContent = {};
                    }
                }
                else if (response.result == "01") {
                    if (easyconf.isInsertView()) {
                        easyconf.setPrimaryKeyContent();
                        easyconf.setUniqueError();
                    }
                }
                easyconf.unLock();
            }
        );
    },

    getSubTitle:function() {
        return this.subTitles[this.view];
    },

    setCandidateAndRange:function() {
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.control == this.controls.CBOX && column.candidate == this.candidates.FIXED) {
                kv = {};
                range = [];
                fixed = column.fixed;
                for (var j=0; j<fixed.length; j++) {
                    key = fixed[j].key;
                    value = fixed[j].value;
                    kv[key] = value;
                    range.push({"key":key, "value":value});
                }
                this.candidate[column.id] = kv;
                this.range[column.id] = range;
            }
        }
    },

    isListView:function() {
        return (this.view == this.views.LIST);
    },

    isUpdateView:function() {
        return (this.view == this.views.UPDATE);
    },

    isInsertView:function() {
        return (this.view == this.views.INSERT);
    },

    getCol:function(isOnlyShow) {
        var ret = [];
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (!isOnlyShow || column.isShow) {
                ret.push(column.id);
            }
        }
        return ret;
    },

    getShowCol:function() {
        return this.getCol(true);
    },

    getAllCol:function() {
        return this.getCol(false);
    },

    getPKV:function(row) {
        var ret = {};
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.isPrimaryKey) {
                ret[column.id] = row[column.id];
            }
        }
        return ret;
    },

    getDetailPKV:function() {
        return this.getPKV(this.detailContent);
    },

    getRGV:function(column) {
        var ret = {};
        where = column.flexible.where;
        for (var i=0; i<where.length; i++) {
            ret[where[i]] = this.detailContent[where[i]];      
        }
        return ret;
    },

    formatRangeUrl:function(column) {
        ret = "./" + this.apps.RANGE + ".jsp?table=" + column.flexible.table + "&key=" + column.flexible.key + "&value=" + column.flexible.value + "&query=" + JSON.stringify(this.getRGV(column));
        return ret;
    },

    formatInitUrl:function(table) {
        ret = "./" + this.apps.INIT + ".jsp?table=" + table;
        return ret;
    },

    formatListHomeUrl:function(data, query) {
        ret = "./" + this.apps.LIST + ".jsp?table=" + this.table + "&data=" + JSON.stringify(data) + "&query=" + JSON.stringify(query)
        this.queryUrl = ret;
        ret += "&begin=0" + "&count=" + this.conf.count;
        return ret;
    },

    formatListNewUrl:function() {
        ret = this.queryUrl + "&begin=" + this.cursor + "&count=" + this.conf.count;
        return ret
    },

    formatDetailUrl:function(query) {
        ret = "./" + this.apps.DETAIL + ".jsp?table=" + this.table + "&query=" + JSON.stringify(query);
        return ret;
    },

    formatDeleteUrl:function(query) {
        ret = "./" + this.apps.DELETE + ".jsp?table=" + this.table + "&query=" + JSON.stringify(query);
        return ret;
    },


    formatUpdateUrl:function(query) {
        ret = "./" + this.apps.UPDATE + ".jsp?table=" + this.table + "&data=" + JSON.stringify(this.detailContent) + "&query=" + JSON.stringify(query);
        return ret;
    },

    formatInsertUrl:function(query) {
        ret = "./" + this.apps.INSERT + ".jsp?table=" + this.table + "&data=" + JSON.stringify(this.detailContent);
        return ret;
    },

    lock:function() {
        this.disabled = true;
    },

    unLock:function() {
        this.disabled = false;
    },

    formatListContent:function(row, column) {
        value = row[column.id];
        ret = (column.control < this.controls.CBOX) ? value : (row[column.id] + "-" + this.candidate[column.id][value]);
        return ret;
    },

    getContent:function(data, isList) {
        var ret = {};
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            content = data[i];
            if (column.control == this.controls.CBOX && column.candidate == this.candidates.FLEXIBLE) {
                idx = content.search(this.sep);
                keyContent = content.substr(0, idx+1);
                valueContent = content.substr(idx+2);
                ret[column.id] =  keyContent;
                if (isList) {
                    if (this.candidate[column.id] == null) {
                        this.candidate[column.id] = {};
                    }
                    this.candidate[column.id][keyContent] = valueContent;
                }
                else {
                    this.range[column.id] = [{"key":keyContent, "value":valueContent}];
                }
            }
            else {
                ret[column.id] = content;
            }
        }
        return ret;
    },

    setListContent:function(data) {
        this.listContent = [];
        for (var i=0; i<data.length; i++) {
            this.listContent.push(this.getContent(data[i], true));
        }
    },

    setDetailContent:function(data) {
        this.detailContent = this.getContent(data, false);
    },

    setPrimaryKeyContent:function() {
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.isPrimaryKey) {
                this.primaryKeyContent[column.id] = this.detailContent[column.id];
            }
        }
    },

    isSearched:function() {
        return (this.queryUrl != null);
    },

    getCurPage:function() {
        return this.conf == null ? -1 : ((this.cursor / this.conf.count) + 1);
    },

    isInt:function(num) {
        return (Math.floor(num) == num)
    },

    isPstInt:function(num) {
        return (this.isInt(num) && num > 0);
    },

    setPrevCursor:function() {
        if ((this.cursor - this.conf.count) < 0) {
            this.cursor = 0;
        }
        else {
            this.cursor -= this.conf.count;
        }
    },

    setNextCursor:function() {
        this.cursor += this.conf.count;
    },

    setGoCursor:function(page) {
        this.cursor = (page-1) * this.conf.count;
    },

    isColText:function(column) {
        return (column.control == this.controls.TEXT);
    },

    isColRadio:function(column) {
        return (column.control == this.controls.RADIO);
    },
    
    isColCbox:function(column) {
        return (column.control == this.controls.CBOX);
    },

    isDetailDisable:function(column) {
        return (this.view < this.views.UPDATE || (this.view == this.views.UPDATE && column.isPrimaryKey));
    },

    isMsgShow:function() {
        return (this.view > this.views.DETAIL);
    },

    isCommitShow:function() {
        return (this.view > this.views.DETAIL);
    },

    isBlank:function(column) {
        value = this.detailContent[column.id];
        return (value == null || String(value).length == 0); 
    },

    checkNull:function(column) {
        if (!column.isNull || column.isPrimaryKey) {
            this.err[column.id] = false;
            this.errmsg[column.id] = this.msgs.NOTNULL;
            return false;
        }
        return true;
    },

    checkType:function(column) {
        value = this.detailContent[column.id];
        switch (column.type) {
            case 0://String
                break;
            case 1://Integer
                if (Math.floor(value) != value) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.INTEGER;
                    return false;
                }
                break;
            case 2://Double
                if (isNaN(value)) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.DOUBLE;
                    return false;
                }
                break;
            case 3://Boolean
                if (column.control != this.controls.RADIO) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.TYPEERROR;
                    return false;
                }
                break;
            case 4://Date
                if (!this.isDate(value)) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.DATE;
                    return false;
                }
                break;
            default:
                this.err[column.id] = false;
                this.errmsg[column.id] = this.msgs.TYPEERROR;
                return false;
                break;
        }
        return true;
    },

    isDate:function(value) {
        console.log(value);
        return this.dateRE.test(value);
    },

    selfCheck:function(column) {
        value = this.detailContent[column.id];
        check = column.check;
        for (var i=0; i<check.length; i++) {
            func = check[i];
            shell = "easyconf." + func.funcname + "(\"" + value + "\"";
            argument = func.argument;
            for (var j=0; j<argument.length; j++) {
                arg = this.detailContent[argument[i]];
                if (arg == null) {
                    arg = "";
                }
                shell += ", \"" + arg + "\"";
            }
            shell += ")";
            console.log("[shell]" + shell);
            if (!eval(shell)) {
                this.err[column.id] = false;
                this.errmsg[column.id] = func.errmsg;
                return false;
            }
        }
        return true;
    },

    isUnique:function() {
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.isPrimaryKey) {
                if (this.primaryKeyContent[column.id] == null || this.detailContent[column.id] != this.primaryKeyContent[column.id]) {
                    return true;
                }
            }
        }
        return false;
    },

    setUniqueMsg:function(isCorrect) {
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.isPrimaryKey) {
                if (isCorrect) {
                    if (this.primaryKeyContent[column.id] != null && this.detailContent[column.id] == this.primaryKeyContent[column.id]) {
                        this.err[column.id] = true;
                        this.errmsg[column.id] = this.msgs.OK
                    }
                }
                else {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.NOTUNIQUE;
                }
            }
        }
    },

    setUniqueCorrect:function() {
        this.setUniqueMsg(true);
    },

    setUniqueError:function() {
        this.setUniqueMsg(false);
    },

    checkUnique:function() {
        if (!this.isUnique()){
            this.setUniqueError();
            return false;
        }
        this.setUniqueCorrect();
        return true;
    },

    setOK:function(column) {
        this.err[column.id] = true;
        this.errmsg[column.id] = this.msgs.OK;
    },

    allOK:function() {
        var i = 0;
        for (k in this.err) {
            if (!this.err[k]) {
                return false;
            }
            i += 1;
        }
        if (i == 0) {
            return false;
        }
        else {
            return true;
        }
    },

    allCheck:function() {
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            this.check[column.id]();
        }
    },

    isColFlexible:function(column) {
        return (column.candidate == this.candidates.FLEXIBLE);
    },

    // user's function here
    testfunc1:function(arg1, arg2, arg3) {
        return true;
    },

    testfunc2:function(arg1, arg2) {
        return true;
    }
});
