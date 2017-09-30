## Sigar-all-in-one 

Sigar和平台依赖的库一起打包


### 使用指南


+ git clone https://github.com/broncho/sigar-all-in-one.git

+ cd sigar-all-in-one

+ mvn install


+ 添加依赖

```
<dependency>
    <groupId>org.fusesource</groupId>
    <artifactId>sigar</artifactId>
    <version>1.6.4</version>
</dependency>
```

+ 使用

```
    public static void main(String[] args) {
    
        Sigar sigar = GoSigar.sigar();
        try {
            System.out.println(sigar.getCpu());
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

```

### 资源

+ [sigar git首页](https://github.com/hyperic/sigar)

+ [sigar 访问操作系统和硬件信息](https://arviarya.wordpress.com/2013/09/22/sigar-access-operating-system-and-hardware-level-information/)

