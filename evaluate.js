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
        EVALUATE:"/servlet/Evaluate"
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

    timeCount:114,
    timeSecond:5,

    radarData:[
        [0, 0.01, 0.01, 0.01, 0.01, 0.01, 0.02, 0.03, 0.03, 0.03, 0.04, 0.05, 0.06, 0.07, 0.07, 0.08, 0.1, 0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.16, 0.17, 0.18, 0.18, 0.2, 0.2, 0.21, 0.42, 0.43, 0.44, 0.47, 0.5, 0.53, 0.56, 0.6, 0.65, 0.67, 0.7, 0.76, 0.81, 0.89, 0.93, 0.95, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
        [0, 0, 0, 0, 0, 0, 0.01, 0.02, 0.02, 0.03, 0.04, 0.06, 0.08, 0.09, 0.1, 0.12, 0.13, 0.15, 0.18, 0.2, 0.22, 0.22, 0.24, 0.26, 0.29, 0.3, 0.31, 0.32, 0.35, 0.37, 0.4, 0.41, 0.45, 0.46, 0.48, 0.5, 0.52, 0.55, 0.58, 0.6, 0.63, 0.65, 0.68, 0.7, 0.71, 0.74, 0.76, 0.79, 0.81, 0.85, 0.88, 0.9, 0.93, 0.97, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0.18, 0.25, 0.32, 0.4, 0.47, 0.55, 0.61, 0.69, 0.76, 0.82, 0.9, 0.96, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
        [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.2, 0.4, 0.7, 0.8, 0.8, 0.7, 0.65, 0.6, 0.8, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.85, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 1, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.87, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9],
        [0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0 , 0.0, 0.0, 0.0, 0.05, 0.05 , 0.1, 0.1, 0.15, 0.15, 0.2 , 0.2, 0.25, 0.25, 0.3, 0.35 , 0.4, 0.4, 0.45, 0.45, 0.45 , 0.5, 0.5, 0.5, 0.55, 0.6 , 0.65, 0.65, 0.65, 0.65, 0.65 , 0.65, 0.7, 0.7, 0.7, 0.75 , 0.75, 0.8, 0.8, 0.9, 0.9 , 0.9]
    ],

    chartData:[
        [0, 0.01, 0.01, 0.01, 0.01, 0.02, 0.03, 0.03, 0.04, 0.05, 0.06, 0.07, 0.07, 0.08, 0.09, 0.09, 0.1, 0.12, 0.17, 0.2, 0.24, 0.3, 0.35, 0.4, 0.47, 0.49, 0.55, 0.59, 0.62, 0.68, 0.73, 0.77, 0.8, 0.83, 0.85, 0.87, 0.89, 0.9, 0.92, 0.93, 0.94, 0.95, 0.96, 0.97, 0.97, 0.98, 0.99, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
    ],

    timeline:{
        realtime:false,
        data:[],
        controlPosition:"none",
        x:"100%",
        y:"100%",
        label:{
            formatter:function(s) {
                return (s*5);
            }
        },
        autoPlay:true,
        loop:false,
        playInterval:100
    },

    chartOptions: [
        {
            options:[
                {
                    title:{text:"chart"},
                    tooltip:{
                        trigger: 'axis'
                    },
                    series:[
                        {
                            type: 'pie',
                            data: [{value:1, name:"remain"}, {value:0, name:"occupy"}]
                        }
                    ]
                }
            ]
        }
    ],

    lineOption:{
        options:[
            {
                title:{text:"line"},
                legend:{
                    show:true,
                    x:"center",
                    y:"top",
                    data:[]
                },
                tooltip:{
                    trigger: 'axis'
                },
                xAxis: [
                    {
                        type: 'category',
                        data:[]
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        min: -0.1,
                        max: 1.1
                    }
                ],
                series:[]
            }
        ]
    },

    radarOption:{
        options:[
            {
                title:{text:"radar"},
                tooltip:{
                    trigger: 'axis'
                },
                polar:[
                   {
                       indicator:[
                        ]
                    }
                ],
                series:[
                    {
                        type: 'radar',
                        data:
                            [
                                {
                                    value:[],
                                    name:'ÆÀ¹ÀµÃ·Ö'
                                }
                            ]
                    }
                ]
            }
        ]
    },

    graphOption:{
        title: {text:"graph"},
        tooltip: {},
        legend:[{data:[]}],
        animationDuration: 1500,
        animationEasingUpdate: "quinticInOut",
        series:[
            {
                type: "graph",
                layout: "force",
                lineStyle:{
                    normal:{
                        type:"dashed",
                    }
                },
                data: [],
                links: [],
                categories: [],
                roam: true,
                label: {
                    normal: {
                        position: "right"
                    }
                },
                force: {
                    initLayout: "circular",
                    repulsion: 100
                }
            }
        ]
    },

    lines:["line1", "line2", "line3", "line4", "line5"],
    charts:["chart1"],

    table:"evaluate",
    key:"id",
    value:"name",

    // basic function here, pls do not modify them
    init:function() {
        ev = this;

        ev.id = "";
        ev.graphOption.legend[0].data = ev.categories.map(function (a) { return a.name });
        ev.graphOption.series[0].categories = ev.categories;
        ev.isEvaluated = false;

        ev.isInit = true;
        ev.chartDict = {};

    },

    isNull:function(value) {
        return (value == null || String(value).length == 0);
    },

    setGraph:function(graph) {
        ev.graph = graph;
    },

    setRadar:function(radar) {
        ev.radar = radar;
    },

    setLine:function(line) {
        ev.line = line;
    },

    setChart:function(name, chart) {
        ev.chartDict[name] = chart;
    },

    setTimeline:function() {
        for (var i=0; i<ev.timeCount+1; i++) {
            ev.timeline.data.push(i*ev.timeSecond);
        }
    },

    setLineOption:function() {
        ev.lineOption.timeline = ev.timeline;
        ev.lineOption.options = ev.lineOption.options.slice(0, 1);

        for (var i=0; i<ev.lines.length; i++) {
            ev.lineOption.options[0].series.push({name:ev.lines[i], type:"line", data:[0]})
            ev.lineOption.options[0].xAxis[0].data = ev.timeline.data;
            ev.lineOption.options[0].legend.data.push(ev.lines[i]);
        }

        for (var j=0; j<ev.timeCount; j++) {
            option = {
                series:[]
            };
            for (var i=0; i<ev.lines.length; i++) {
                option.series.push({name:ev.lines[i], type:"line", data:[0].concat(ev.radarData[i].slice(0, j+1))});
            }
            ev.lineOption.options.push(option);
        }
//        console.log(ev.lineOption)
        ev.line.setOption(ev.lineOption);
    },

    setChartOption:function() {
        for (var i=0; i<ev.charts.length; i++) {
            ev.chartOptions[i].timeline = ev.timeline;
            ev.chartOptions[i].options = ev.chartOptions[i].options.slice(0, 1);
            for (var j=0; j<ev.timeCount; j++) {
                if (ev.chartOptions[i].options[0].series[0].type == "pie") {
                    option = {
                        series:[{data:[{name:"remain", value:1-ev.chartData[i][j]}, {name:"occupy", value:ev.chartData[i][j]}]}]
                    };
                    ev.chartOptions[i].options.push(option);
                }
            }
//            console.log(ev.chartOptions[i]);
            ev.chartDict[ev.charts[i]].setOption(ev.chartOptions[i]);
        }
    },

    setRadarOption:function() {
        ev.radarOption.timeline = ev.timeline;
        ev.radarOption.options = ev.radarOption.options.slice(0, 1);

        for (var i=0; i<ev.lines.length; i++) {
            ev.radarOption.options[0].polar[0].indicator.push({text:ev.lines[i], max:1});
            ev.radarOption.options[0].series[0].data[0].value.push(0);
        }


        for (var j=0; j<ev.timeCount; j++) {
            option = {
                series:[{data:[]}]
            };
            for (var i=0; i<ev.lines.length; i++) {
                option.series[0].data.push(ev.radarData[i][j]);
            }
            ev.radarOption.options.push(option);
        }
//        console.log(ev.radarOption);
        ev.radar.setOption(ev.radarOption);
    },

    setGraphOption:function(response) {
        data = [];
        links = [];

        for (var i=0; i<response.vertex.length; i++) {
            vertex = response.vertex[i];
            data.push({id:vertex.id, itemStyle:null, label:{normal:{show:true}}, name:vertex.name + "[" + vertex.sScore + "]", value:vertex.dScore, category:ev.categoryIndex[vertex.type], symbolSize:vertex.dScore * 20});
        }

        for (var i=0; i<response.edge.length; i++) {
            edge = response.edge[i];
            links.push({source:edge.child, target:edge.parent, value:edge.weight});
        }

        ev.graphOption.series[0].data = data;
        ev.graphOption.series[0].links = links;
        ev.graphOption.series[0].name = response.experimentInfo.name;
        console.log(ev.graphOption)
        ev.graph.setOption(ev.graphOption);
    },

    showLoading:function(http) {
        ev.graph.showLoading();
    },

    hideLoading:function(http) {
        ev.graph.hideLoading();
    },

    commit:function(http) {
        console.log("evaluate...");
        
        ev.lock();
        ev.showLoading();
        
        http.get(ev.apps.EVALUATE, {params:{id:ev.id, isReadCache:ev.isReadCache}}).success(
//        url = "./evaluate.json";
//        http.get(url).success(
            function(response) {
                ev.hideLoading();
                ev.experimentInfo = response.experimentInfo;
                ev.setGraphOption(response)
                ev.setTimeline();
                ev.setRadarOption();
                ev.setLineOption();
                ev.setChartOption();
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
    },
});
