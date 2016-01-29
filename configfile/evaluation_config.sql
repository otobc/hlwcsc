create database evaluation_config;
use evaluation_config;
drop table if exists wpindex;
drop table if exists vertex;
drop table if exists edge;
drop table if exists evaluate;
drop table if exists rawdata;
drop table if exists wpidxcache;
drop table if exists experiment;

create table wpindex(
    id		varchar(64)		not null,
    name	varchar(256) 	not null,
    expression  varchar(1024)   not null,
    primary key (id)
);

create table vertex (
    evaluateid  varchar(64)         not null,
    id		varchar(64)		not null,
    name	varchar(256)	not null,
    class	char(1)		not null,
    vertextype  char(1)		not null,
    wpindexid varchar(64),
    nmidx	char(2),
    nmidxexp	varchar(1024),
    vertexlevel varchar(64),
    nmlvl	char(2),
    nmlvlexp	varchar(1024),
    mbsp	varchar(1024),
    conclude	char(2),
    weighting char(2),
    primary key (evaluateid,id)
);

create table edge (
    evaluateid	varchar(64)		not null,
    child	varchar(64)		not null,
    parent	varchar(64)		not null,
    weight	double(9,8)	not null,
    primary key (evaluateid,child,parent)
);

create table evaluate (
    id		varchar(64)		not null,
    name	varchar(256)	not null,
    expid	varchar(64)		not null,
    primary key (id)
);

create table rawdata (
    id		varchar(64)		not null,
    name	char(8)		not null,
    environmentid varchar(64)	not null,
    nodeid	varchar(64)		not null,
    time	char(23)	not null,
    datatype	char(1)		not null,
    value	varchar(256),
    primary key (id,environmentid,nodeid,time)
);

create table wpidxcache (
    evaluateid	varchar(64)		not null,
    wpindexid	varchar(64)		not null,
    experimentid varchar(64)	not null,
    datatype	char(1)		not null,
    value	varchar(256),
    primary key (evaluateid,wpindexid,experimentid)
);

create table experiment (
    id		varchar(64)		not null,
    name 	varchar(256)	not null,
    wptype	char(1)		not null,
    tester	varchar(256)	not null,
    bgtime	char(23)		not null,
    edtime	char(23)		not null,
    primary key (id)
);






INSERT INTO `evaluation_config`.`experiment` (`id`, `name`, `wptype`, `tester`, `bgtime`, `edtime`) VALUES ('001', '演示', '0', '008', '2016-01-29 10:00:00.00', '2016-01-29 10:03:00.00');



INSERT INTO `evaluation_config`.`evaluate` (`id`, `name`, `expid`) VALUES ('001', '演示', '001');



INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '000', '最终得分', '0', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '100', '僵尸网络性能', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '200', '木马武器性能', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '300', '靶标致瘫状态', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '110', '肉鸡利用率', '2', '0', '110', '00', 'x/44', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '120', '抗追踪能力', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '130', '损耗性', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '210', '武器攻击力', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '220', '武器可控力', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '310', '功能致瘫', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `weighting`) VALUES ('001', '320', '资源性能', '1', '0', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '121', '肉鸡组织结构', '2', '0', '121', '00', 'x>=3?0.9:(x==2?0.6:0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '122', '肉鸡IP伪装', '2', '0', '122', '00', 'x', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '123', '主服务器隐藏性', '2', '0', '123', '00', 'x', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '131', '损坏率', '2', '0', '131', '00', 'x/44', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '132', '肉鸡平均CPU状态指数', '2', '0', '132', '00', '(x-0.2)/(1-0.2)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '133', '肉鸡平均内存状态指数', '2', '0', '133', '00', '(x-0.2)/(1-0.2)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '134', '肉鸡网络带宽状态指数', '2', '0', '134', '00', '(x-0.3)/(1-0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '211', '攻击多样性', '2', '0', '211', '00', 'x>=3?0.9:(x==2?0.6:0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '212', '参数可变性', '2', '0', '212', '00', 'x>=3?0.9:(x==2?0.6:0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '213', '靶标类型多样性', '2', '0', '213', '00', 'x>=3?0.9:(x==2?0.6:0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '221', '通信信道可替换性', '2', '0', '221', '00', 'x>=3?0.9:(x==2?0.6:0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '222', '通信是否加密', '2', '0', '222', '00', 'x', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '311', '功能致瘫速度', '2', '0', '311', '00', 'x<=5?0.9:(x<=10?0.6:0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '312', '致瘫恢复性', '2', '0', '312', '00', 'x==0?0:(x>10?0.5:1)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '321', '服务器CPU状态指数', '2', '0', '321', '00', '(x-0.3)/(1-0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '322', '服务器内存转态指数', '2', '0', '322', '00', '(x-0.2)/(1-0.2)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '323', '服务进程比', '2', '0', '323', '00', 'x/50', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '324', '网络链路转态指数', '2', '0', '324', '00', '(x-0.3)/(1-0.3)', '01');
INSERT INTO `evaluation_config`.`vertex` (`evaluateid`, `id`, `name`, `class`, `vertextype`, `wpindexid`, `nmidx`, `nmidxexp`, `weighting`) VALUES ('001', '325', '服务器丢包率', '2', '0', '325', '00', 'x', '01');


INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('110', '有效肉鸡数量', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('121', '肉鸡组织结构', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('122', '肉鸡IP是否可伪造', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('123', '主服务器隐藏层数', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('131', '损坏肉鸡数量', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('132', '肉鸡平均CPU使用率', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('133', '肉鸡平均内存使用率', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('134', '肉鸡网络带宽使用率', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('211', '攻击方式总数', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('212', '可变参数总数', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('213', '靶标类型总数', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('221', '可替换信道数量', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('222', '通信是否加密', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('311', '致瘫所需时间(min)', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('312', '恢复所需时间(min)', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('321', '服务器CPU使用率', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('322', '服务器内存使用率', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('323', '服务器进程数量', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('324', '服务器网络链路使用率', 'N');
INSERT INTO `evaluation_config`.`wpindex` (`id`, `name`, `expression`) VALUES ('325', '服务器丢包率', 'N');



INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '110', '001', '2', '30');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '121', '001', '2', '2');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '122', '001', '2', '1');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '123', '001', '2', '2');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '131', '001', '2', '3');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '132', '001', '2', '0.6');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '133', '001', '2', '0.7');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '134', '001', '2', '0.8');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '211', '001', '2', '2');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '212', '001', '2', '0');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '213', '001', '2', '2');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '221', '001', '2', '2');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '222', '001', '2', '1');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '311', '001', '2', '7');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '312', '001', '2', '20');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '321', '001', '2', '0.9');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '322', '001', '2', '0.8');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '323', '001', '2', '39');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '324', '001', '2', '0.8');
INSERT INTO `evaluation_config`.`wpidxcache` (`evaluateid`, `wpindexid`, `experimentid`, `datatype`, `value`) VALUES ('001', '325', '001', '2', '0.9');



INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '100', '000', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '200', '000', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '300', '000', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '110', '100', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '120', '100', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '130', '100', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '210', '200', '0.6');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '220', '200', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '310', '300', '0.6');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '320', '300', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '121', '120', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '122', '120', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '123', '120', '0.5');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '131', '130', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '132', '130', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '133', '130', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '134', '130', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '211', '210', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '212', '210', '0.3');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '213', '210', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '221', '220', '0.6');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '222', '220', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '311', '310', '0.4');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '312', '310', '0.6');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '321', '320', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '322', '320', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '323', '320', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '324', '320', '0.2');
INSERT INTO `evaluation_config`.`edge` (`evaluateid`, `child`, `parent`, `weight`) VALUES ('001', '325', '320', '0.2');
