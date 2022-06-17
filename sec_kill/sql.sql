create or replace table system_menu
(
    id          int(11) unsigned auto_increment comment 'ID'
        primary key,
    pid         int(11) unsigned    default 0       not null comment '父ID',
    title       varchar(100)        default ''      not null comment '名称',
    icon        varchar(100)        default ''      not null comment '菜单图标',
    href        varchar(100)        default ''      not null comment '链接',
    target      varchar(20)         default '_self' not null comment '链接打开方式',
    sort        int                 default 0       null comment '菜单排序',
    deleted     tinyint(1) unsigned default 0       not null comment '逻辑删除(1:禁用,0:启用)',
    create_time timestamp                           null comment '创建时间',
    remark      varchar(255)                        null comment '备注信息',
    update_time timestamp                           null comment '更新时间',
    delete_at   timestamp                           null comment '删除时间',
    version     int                 default 0       null comment '乐观锁'
)
    comment '系统菜单表' auto_increment = 253;

create or replace index href
    on system_menu (href);

create or replace index title
    on system_menu (title);

create or replace table t_goods
(
    id             bigint auto_increment
        primary key,
    goods_name     varchar(64)       null comment '商品名称',
    goods_price    decimal(10, 2)    null comment '商品价格',
    goods_img      varchar(64)       null comment '商品图片',
    goods_pic      varchar(64)       null comment '图片名称',
    goods_title    varchar(64)       null comment '商品标题',
    goods_stock    int               null comment '商品库存 -1标识没有限制',
    goods_detail   varchar(255)      null comment '商品详情',
    create_user_id bigint            null comment '创建者id',
    create_time    datetime          null comment '商品创建时间',
    update_time    datetime          null comment '商品信息修改时间',
    deleted        tinyint default 0 null comment '逻辑删除',
    version        int     default 0 null comment '乐观锁'
)
    charset = utf8mb4
    auto_increment = 15;

create or replace table t_order
(
    id            bigint auto_increment
        primary key,
    user_id       bigint            null comment '用户id',
    goods_id      bigint            null comment '商品id',
    delivery_addr varchar(255)      null comment '收货地址',
    goods_name    varchar(64)       null comment '商品名称',
    goods_count   int               null comment '商品数量',
    goods_price   decimal(10, 2)    null comment '商品价格',
    order_channel int(4)            null comment '1:pc 2: android 3: ios',
    status        int(4)            null comment '0:未支付 1：已支付 2：已发货 3：已收货 4：已退款',
    create_time   datetime          null comment '订单创建时间',
    update_time   datetime          null comment '订单最近修改时间',
    pay_time      datetime          null comment '支付时间',
    type          tinyint default 0 null comment '0:普通商品 1:秒杀商品',
    version       int     default 0 null comment '乐观锁',
    deleted       tinyint default 0 null comment '逻辑删除'
)
    charset = utf8mb4
    auto_increment = 55945;

create or replace table t_seckill_goods
(
    id            bigint auto_increment
        primary key,
    goods_id      bigint            null comment '商品id',
    seckill_price decimal(10, 2)    null comment '秒杀价格',
    stock_count   int               null comment '秒杀库存',
    start_time    datetime          null comment '秒杀开始时间',
    end_time      datetime          null comment '秒杀结束时间',
    create_time   datetime          null comment '秒杀商品创建时间',
    update_time   datetime          null comment '秒杀商品修改时间',
    deleted       tinyint default 0 null comment '逻辑删除',
    version       int     default 0 null comment '乐观锁'
)
    charset = utf8mb4
    auto_increment = 3;

create or replace table t_seckill_order
(
    id       bigint auto_increment
        primary key,
    user_id  bigint null comment '用户iD',
    order_id bigint null comment '订单id',
    goods_id bigint null comment '商品id',
    constraint t_seckill_order_user_id_goods_id_uindex
        unique (user_id, goods_id)
)
    charset = utf8mb4
    auto_increment = 55932;

create or replace table t_user
(
    id              bigint            not null comment 'id'
        primary key,
    user_name       varchar(255)      null comment '用户名',
    pass_word       varchar(255)      null comment '密码',
    salt            varchar(255)      null comment '盐值',
    create_time     datetime          null comment '注册时间',
    update_time     datetime          null comment '最近一次修改时间',
    last_login_time datetime          null comment '最后一次登录时间',
    role            varchar(255)      null comment '角色',
    perms           varchar(255)      null comment '权限',
    deleted         tinyint default 0 null comment '逻辑删除',
    version         int     default 0 null comment '乐观锁'
)
    charset = utf8mb4;

