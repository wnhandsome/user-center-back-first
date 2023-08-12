create table user
(
    id           int auto_increment comment 'id主键'
        primary key,
    username     varchar(256)                       null comment '（用户名）昵称',
    user_account varchar(256)                       not null comment '用户账号',
    password     varchar(256)                       not null comment '密码',
    avatar_url   varchar(1024)                      null comment '头像地址',
    phone        varchar(256)                       null comment '电话',
    email        varchar(256)                       null comment '邮箱',
    user_status  int      default 0                 not null comment '用户状态 (0-正常,1-禁用)',
    gender       tinyint                            null comment '性别',
    role         int      default 0                 null comment '用户角色（0-普通用户,1-管理员）',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间 (数据插入)',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间 (数据更新)',
    is_delete    tinyint  default 0                 not null comment '是否删除 0 1 (逻辑删除)',
    constraint user_email_uindex
        unique (email),
    constraint user_phone_uindex
        unique (phone),
    constraint user_user_account_uindex
        unique (user_account)
)
    comment '用户表';
