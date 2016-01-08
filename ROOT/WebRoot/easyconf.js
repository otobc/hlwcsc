var easyconf = new Object({
    // basic definition here, pls do not modify them 
    subTitles:['LIST', 'DETAIL', 'UPDATE', 'NEW'],
    msgs:{
        OK:"OK",
        NN:"NOT NULL",
        INT:"MUST INPUT INTEGER",
        DB:"MUST INPUT NUMBER",
        TP:"TYPE ERROR",
        DT:"MUST INPUT DATE:[YYYY-MM-DD hh:mm:ss.ms]",
        NU:"NOT UNIQUE"
    },
    dateRE:RegExp("^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2]\\d|3[0-1]) ([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d\\.\\d{2}$"),
    listcontent:[],
    detailcontent:{},
    view:0,
    cursor:0,
    candidate:{},
    queryUrl:null,
    apps:{
        LIST:"list",
        DETAIL:"detail",
        DELETE:"delete",
        COMMIT:"commit",
        ISUNIQUE:"isunique"
    },
    err:{},
    errmsg:{},
    check:{},

    // basic function here, pls do not modify them
    getSubTitle:function() {
        return this.subTitles[this.view];
    },

    setCandidate:function() {
        var columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.control == 2) {
                kv = {};
                range = column.range;
                for (var j=0; j<range.length; j++) {
                    kv[range[j].key] = range[j].value;
                }
                this.candidate[column.id] = kv;
            }
        }
    },

    isListView:function() {
        return (this.view == 0);
    },

    isUpdateView:function() {
        return (this.view == 2);
    },

    isNewView:function() {
        return (this.view == 3);
    },

    getCol:function(isOnlyShow) {
        ret = [];
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

    getPKV:function(row, isRowList) {
        ret = {};
        columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.isPrimaryKey) {
                ret[column.id] = isRowList ? row[column.index] : row[column.id];
            }
        }
        return ret;
    },

    getPKVFromList:function(row) {
        return this.getPKV(row, true);
    },

    getPKVFromDict:function(row) {
        return this.getPKV(row, false);
    },

    getDetailPKV:function() {
        return this.getPKVFromDict(this.detailcontent);
    },

    formatListHomeUrl:function(data, query) {
        ret = "./" + this.apps.LIST + ".jsp?table=" + this.conf.table + "&data=" + JSON.stringify(data) + "&query=" + JSON.stringify(query)
        this.queryUrl = ret;
        ret += "&begin=0" + "&count=" + this.conf.count;
        return ret;
    },

    formatListNewUrl:function() {
        ret = this.queryUrl + "&begin=" + this.cursor + "&count=" + this.conf.count;
        return ret
    },

    formatDetailUrl:function(query) {
        ret = "./" + this.apps.DETAIL + ".jsp?table=" + this.conf.table + "&query=" + JSON.stringify(query);
        return ret;
    },

    formatDeleteUrl:function(query) {
        ret = "./" + this.apps.DELETE + ".jsp?table=" + this.conf.table + "&query=" + JSON.stringify(query);
        return ret;
    },

    formatIsUniqueUrl:function(query) {
        ret = "./" + this.apps.ISUNIQUE + ".jsp?table=" + this.conf.table + "&query=" + JSON.stringify(query);
        return ret;
    },

    formatCommitUrl:function(query, dest) {
        ret = "./" + this.apps.COMMIT + ".jsp?table=" + this.conf.table + "&dest=" + dest + "&data=" + JSON.stringify(this.detailcontent) + "&query=" + JSON.stringify(query);
        return ret;
    },

    formatUpdateCommitUrl:function(query) {
        return this.formatCommitUrl(query, 0);
    },

    formatNewCommitUrl:function(query) {
        return this.formatCommitUrl("", 1);
    },

    lock:function() {
        this.disabled = true;
    },

    unLock:function() {
        this.disabled = false;
    },

    formatListContent:function(row, column) {
        var ret = (column.control < 2) ? row[column.index] : (row[column.index] + "-" + this.candidate[column.id][row[column.index]]);
        return ret;
    },

    setDetailContent:function(data) {
        var columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            this.detailcontent[column.id] = data[column.index];
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
        return (column.control == 0);
    },

    isColRadio:function(column) {
        return (column.control == 1);
    },
    
    isColCbox:function(column) {
        return (column.control == 2);
    },

    isDetailDisable:function(column) {
        return (this.view < 2 || (this.view == 2 && column.isPrimaryKey));
    },

    isMsgShow:function() {
        return (this.view > 1);
    },

    isCommitShow:function() {
        return (this.view > 1);
    },

    isBlank:function(column) {
        var value = this.detailcontent[column.id];
        return (value == null || String(value).length == 0); 
    },

    checkNull:function(column) {
        if (!column.isNull || column.isPrimaryKey) {
            this.err[column.id] = false;
            this.errmsg[column.id] = this.msgs.NN;
            return false;
        }
        return true;
    },

    checkType:function(column) {
        var value = this.detailcontent[column.id];
        switch (column.type) {
            case 0://String
                break;
            case 1://Integer
                if (Math.floor(value) != value) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.INT;
                    return false;
                }
                break;
            case 2://Double
                if (isNaN(value)) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.DB;
                    return false;
                }
                break;
            case 3://Boolean
                if (column.control != 1) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.TP;
                    return false;
                }
                break;
            case 4://Date
                if (!this.isDate(value)) {
                    this.err[column.id] = false;
                    this.errmsg[column.id] = this.msgs.DT;
                    return false;
                }
                break;
            default:
                this.err[column.id] = false;
                this.errmsg[column.id] = this.msgs.TP;
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
        var value = this.detailcontent[column.id];
        var check = column.check;
        for (var i=0; i<check.length; i++) {
            func = check[i];
            shell = "easyconf." + func.funcname + "(\"" + value + "\"";
            argument = func.argument;
            for (var j=0; j<argument.length; j++) {
                arg = this.detailcontent[argument[i]];
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

    setUniqueError:function() {
        var columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            if (column.isPrimaryKey) {
                this.err[column.id] = false;
                this.errmsg[column.id] = this.msgs.NU;
            }
        }
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
        var columns = this.conf.columns;
        for (var i=0; i<columns.length; i++) {
            column = columns[i];
            this.check[column.id]();
        }
    },

    // user's function here
    testfunc1:function(arg1, arg2, arg3) {
        return true
    },

    testfunc2:function(arg1, arg2) {
        return true
    }
});
