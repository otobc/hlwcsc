drop table if exists wpindex;
drop table if exists vertex;
drop table if exists edge;
drop table if exists evaluate;
drop table if exists rawdata;
drop table if exists wpidxcache;
drop table if exists experiment;

create table wpindex(
    id		char(8)		not null,
    name	varchar(256) 	not null,
    expression  varchar(1024)   not null,
    primary key (id)
);

create table vertex (
    evaluateid  char(8)         not null,
    id		char(8)		not null,
    name	varchar(256)	not null,
    class	char(1)		not null,
    vertextype  char(1)		not null,
    wpindexid char(8),
    nmidx	char(2),
    nmidxexp	varchar(1024),
    vertexlevel varchar(64),
    nmlvl	char(2),
    nmlvlexp	varchar(1024),
    mbsp	varchar(1024),
    conclude	char(2),
    primary key (evaluateid,id)
);

create table edge (
    evaluateid	char(8)		not null,
    child	char(8)		not null,
    parent	char(8)		not null,
    weight	double(8,8)	not null,
    weighting	char(2),
    primary key (evaluateid,child,parent)
);

create table evaluate (
    id		char(8)		not null,
    name	varchar(256)	not null,
    expid	char(8)		not null,
    primary key (id)
);

create table rawdata (
    id		char(8)		not null,
    name	char(8)		not null,
    environmentid char(8)	not null,
    nodeid	char(8)		not null,
    datatype	char(1)		not null,
    value	varchar(256),
    time	char(22)	not null,
    primary key (id,environmentid,nodeid)
);

create table wpidxcache (
    evaluateid	char(8)		not null,
    wpindexid	char(8)		not null,
    experimentid char(8)	not null,
    datatype	char(1)		not null,
    value	varchar(256),
    primary key (evaluateid,wpindexid,experimentid)
);

create table experiment (
    id		char(8)		not null,
    name 	varchar(256)	not null,
    wptype	char(1)		not null,
    tester	varchar(256)	not null,
    bgtime	date		not null,
    edtime	date		not null,
    primary key (id)
);
