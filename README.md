# thymeleaf-dynamic-url
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ayongw/thymeleaf-dynamic-url-dialect.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.ayongw%22%20AND%20a:%22thymeleaf-dynamic-url-dialect%22)

动态替换资源url thymeleaf插件
## 解决的问题
    解决在开发环境下配置的资源地址能够在不更改程序代码的前提下自动替换为实际的各环境地址。
    比如开发环境使用本地地址，生产环境可以将地址自动映射到CDN地址上。加快资源文件加载

## 使用示例
HTML示例片段
```
<link href="../static/lib/bootstrap/dist/css/bootstrap.css" awdl:href="@{/static/lib/bootstrap/dist/css/bootstrap.min.css}" rel="stylesheet" />
<link href="../static/custlib/sb-admin/css/sb-admin-2.css" awdl:href="@{/static/custlib/sb-admin/css/sb-admin-2.css}" rel="stylesheet" />
```