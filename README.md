# springboot-devtools-quartz

## 复现springboot-devtools的一个bug


1. springboot-devtools与quartz集成导致的ApplicationContext无法获取bean实例的bug；
2. 还会导致通过AOP获取方法的参数名时，参数名变为arg0/arg1之类的名称；
