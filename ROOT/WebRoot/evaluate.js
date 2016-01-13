var evaluate = new Object({
    // basic definition here, pls do not modify them 
    names:{
        EVALUATE:"EVALUATE",
        EVALUATEID:"EVALUATEID",
        ISREADCACHE:"ISREADCACHE",
        COMMIT:"COMMIT",
        EXPERIMENTID:"EXPERIMENTID",
        EXPERIMENTNAME:"EXPERIMENTNAME",
        WEAPONTYPE:"WEAPONTYPE",
        TESTER:"TESTER",
        BEGINTIME:"BEGINTIME",
        ENDTIME:"ENDTIME"
    },
    apps:{
        EVALUATE:"evaluate"
    },
    categoryIndex:{
        "0":0,
        "1":1,
    },
    categories:[
        {name:"quantitative"},
        {name:"qualitative"}
    ],
    range:{
        isReadCache:[
            {key:1, value:"YES"},
            {key:0, value:"NO"}
        ]
    },
    option:{
        title: {},
        tooltip: {},
        legend: [{data:[]}],
        animationDuration: 1500,
        animationEasingUpdate: 'quinticInOut',
        series : [
            {
                type: 'graph',
                layout: 'none',
                data: [],
                links: [],
                categories: [],
                roam: true,
                label: {
                    normal: {
                        position: 'right'
                    }
                },
                lineStyle: {
                    normal: {
                        curveness: 0.3
                    }
                }
            }
        ]
    },

    // basic function here, pls do not modify them
    init:function() {
        ev = this;

        ev.id = "";
        ev.option.legend[0].data = ev.categories.map(function (a) { return a.name });
        ev.option.series[0].categories = ev.categories;
        ev.isEvaluated = false;

        ev.isInit = true;
    },

    isNull:function(value) {
        return (value == null || String(value).length == 0);
    },

    setChart:function(chart) {
        evaluate.chart = chart;
    },

    setOption:function(response) {
        data = [];
        links = [];

        for (var i=0; i<response.vertex.length; i++) {
            vertex = response.vertex[i];
            data.push({id:vertex.id, itemStyle:null, label:{normal:{show:true}}, name:vertex.name, value:vertex.sScore, category:evaluate.categoryIndex[vertex.type], symbolSize:vertex.dScore * 100, x:0.5, y:0});
        }

        for (var i=0; i<response.edge.length; i++) {
            edge = response.edge[i];
            links.push({source:edge.child, target:edge.parent, value:edge.weight});
        }

        ev.option.series[0].data = data;
        ev.option.series[0].links = links;
    },

    commit:function(http) {
        console.log("evaluate...");
        
        ev.lock();
        ev.chart.showLoading();
//        http.get(ev.apps.EVALUATE, {params:{id:ev.id, isReadCache:ev.isReadCache ? 1 : 0}}).success(
        url = "./evaluate.json";
        http.get(url).success(
            function(response) {
                ev.chart.hideLoading();
                ev.experimentinfo = response.experimentinfo;
                ev.setOption(response);
                console.log(ev.option);
                ev.chart.setOption(ev.option);
                ev.isEvaluated = true;
                ev.unLock();
            }
        );
    },

    lock:function() {
        ev.disabled = true;
    },

    unLock:function() {
        ev.disabled = false;
    }
});
