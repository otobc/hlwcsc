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
    weighing char(2),
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
    datatype	char(1)		not null,
    value	varchar(256),
    time	char(22)	not null,
    primary key (id,environmentid,nodeid)
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
    bgtime	date		not null,
    edtime	date		not null,
    primary key (id)
);
