package com.xl.study.studytest.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaTest {

    /**
     * ============
     * Stream源操作
     * ===========
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class TestObj{
        private String name;
        private Integer age;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Product {

        // 商品id
        private Long id;
        // 商品数量
        private Integer num;
        // 商品价格
        private BigDecimal price;
        // 商品名称
        private String name;
        // 商品种类
        private String category;
    }

    /**
     * =============
     * Stream中间操作
     * ============
     */
    @Test
    public void testaaa(){
        HashMap<String, Integer> map = new HashMap<String, Integer>() {{
            put("xiong", 1);
            put("xiong2",3);
        }};
        System.out.println(map);
        map.merge("xiong",2,(oldVal,newVal) -> oldVal + newVal);
        map.merge("xiong3",2,(oldVal,newVal) -> oldVal + newVal);
        System.out.println(map);

        Map<String, Integer> collect = map.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal, LinkedHashMap::new));
        collect.entrySet().forEach(System.out::println);
    }

    //Stream流的基本操作
    @Test
    public void mapChange(){
        List<String> toupcase = Stream.of("AAAS", "casdAS", "sadsa", "kSDGFD","aaa","aaa").map(String::toUpperCase).skip(4).limit(2).distinct().collect(Collectors.toList());
        System.out.println(toupcase);
        List<Integer> tolength = Stream.of("AAAS", "acasdAS", "sadsa", "ASDGFD").map(String::length).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        System.out.println(tolength);
        TestObj a = new TestObj("a", 1);
        TestObj b = new TestObj("b", 4);
        TestObj c = new TestObj("c", 3);
        ArrayList<TestObj> testObjs = new ArrayList<>(Arrays.asList(a, b, c));
        List<TestObj> testObjMap = testObjs.stream().map(e -> {
            e.setAge(e.getAge() + 1);
            return e;
        }).collect(Collectors.toList());
        System.out.println(testObjMap);
        List<TestObj> testObjPeek = testObjs.stream().peek(e -> {
            e.setAge(e.getAge() + 1);
        }).collect(Collectors.toList());

        testObjPeek.sort(Comparator.comparing(TestObj::getAge).reversed());
        System.out.println(testObjPeek);
    }

    //map的排序
    @Test
    public void mapSortTest(){
        Map<String, Integer> codes = new HashMap<String, Integer>(){{
            // 在初始化的时候,就放入数据
            put("United States", 1);
            put("Germany", 49);
            put("France", 33);
            put("China", 86);
            put("Pakistan", 92);
        }};

// 根据Map的键进行排序
        Map<String, Integer> sortedMap = codes.entrySet().stream()
                // 根据Map的key进行排序
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        // 把Stream流排序后的结果收集为Map
                        Collectors.toMap(
                                // 使用Map原本的key和value
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                // 当主键有相同的情况,发生主键冲突的时候,使用旧主键所对应的值
                                (oldVal, newVal) -> oldVal,
                                // 收集之后的Map值一个LinkedHashMap类型(有顺序的Map)
                                LinkedHashMap::new
                        )
                );
        sortedMap.entrySet().forEach(System.out::println);

        Map<String, Integer> sortedMap2 = codes.entrySet().stream()
                // 根据Map的value进行排序
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));
        sortedMap2.entrySet().forEach(System.out::println);
    }

    //Stream的元素的匹配与查找
    @Test
    public void mathAndSearch(){
        TestObj a = new TestObj("a", 20);
        TestObj b = new TestObj("b", 31);
        TestObj c = new TestObj("c", 35);
        ArrayList<TestObj> testObjs = new ArrayList<>(Arrays.asList(a,c,b));
        // 判断employees列表中,是否有年龄 > 25岁的对象存在
        boolean isExistAgeThan25 = testObjs.stream().anyMatch(e -> e.getAge() > 25);
        System.out.println(isExistAgeThan25);

        // 判断employees列表中,是否所有的对象的年龄 > 10岁
        boolean isExistAgeThan10 = testObjs.stream().allMatch(e -> e.getAge() > 10);
        System.out.println(isExistAgeThan10);

        // 判断employees列表中,是否有年龄 < 18岁的对象存在
        boolean isExistAgeLess18 = testObjs.stream().noneMatch(e -> e.getAge() < 18);
        System.out.println(isExistAgeLess18);

        // 返回第一个年龄 > 40岁的对象(可能有若干个这样的对象,但是只返回第一个)
        Optional<TestObj> employeeOptional =  testObjs.stream().filter(e -> e.getAge() > 30).findFirst();

        System.out.println(employeeOptional.get());

        //查找任意一个符合“匹配规则”的元素,返回值为Optional
        Optional<TestObj> employeeOptionals
                =  testObjs.stream().filter(e -> e.getAge() > 30).findAny();
        System.out.println(employeeOptionals.get());
    }

    //Stream的元素归约
    @Test
    public void reduce(){
        /*
        //Integer类型的归元
        // 1 + 2 + 3 + 4 + 5 + 6
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        int result = numbers
                .stream()
                // 参数1为0,代码累加的初始值
                // 参数2为一个函数,表示元素累加
                .reduce(0, (subtotal, element) -> subtotal + element);
        System.out.println(result);  // 21

        result = numbers
                .stream()
                // 进行归约
                .reduce(0, Integer::sum);
        System.out.println(result); // 21*/

        /*//Sting类型的归元
        List<String> letters = Arrays.asList("a", "b", "c", "d", "e");
        String result = letters
                .stream()
                // 初始值是一个空字符串
                .reduce("", (partialString, element) -> partialString + element);
        System.out.println(result);  // abcde

        result = letters
                .stream()
                .reduce("", String::concat);
        System.out.println(result);  // abcde*/

        TestObj a = new TestObj("a", 20);
        TestObj b = new TestObj("b", 31);
        TestObj c = new TestObj("c", 35);
        ArrayList<TestObj> employees = new ArrayList<>(Arrays.asList(a,c,b));
        Integer total = employees.stream()
                // 先获取出所有对象的年龄
                .map(TestObj::getAge)
                // 对所有的年龄进行归约,求和
                .reduce(0,Integer::sum);
        System.out.println(total);  // 346

        //Combiner合并器的使用
        Integer total3 = employees.stream()
                // 没有使用map来获取对象中的年龄,所以Stream流中的数据实际上是employe对象
                .reduce(0,(totalAge,emp) -> totalAge + emp.getAge(),Integer::sum);
        System.out.println(total3); // 346

    }

    /**
     * ==============
     * 管道流结果处理
     * =============
     */
    @Test
    public void ForMethord(){
        Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
                //数表示对管道中的元素进行并行处理,处理速度更快,但是这样就有可能导致管道流中后面的元素先处理，前面的元素后处理，也就是元素的顺序无法保证
                .parallel()
                .forEach(System.out::println);

        Stream.of("Monkey", "Lion", "Giraffe", "Lemur", "Lion")
                .parallel()
                .forEachOrdered(System.out::println);
    }

    @Test
    public void groupbyTest(){
        // 创建若干个对象,然后放到List中
        Product prod1 = new Product(1L, 1, new BigDecimal("15.5"), "面包", "零食");
        Product prod2 = new Product(2L, 2, new BigDecimal("20"), "饼干", "零食");
        Product prod3 = new Product(3L, 3, new BigDecimal("30"), "月饼", "零食");
        Product prod4 = new Product(4L, 3, new BigDecimal("10"), "青岛啤酒", "啤酒");
        Product prod5 = new Product(5L, 10, new BigDecimal("15"), "百威啤酒", "啤酒");
        List<Product> prodList = Arrays.asList(prod1, prod2, prod3, prod4, prod5);


        Map<String, List<Product>> categoryMap2 = prodList.stream().collect(Collectors.groupingBy(item -> {
            return item.getNum() < 3 ? "小于3" : "other";
        }));
        System.out.println(categoryMap2);

        //根据不同条件分组
        Map<String, Map<String, List<Product>>> categoryMap3 =
                prodList.stream().collect(
                        Collectors.groupingBy(
                                Product::getCategory,Collectors.groupingBy(item -> { return item.getNum() < 3 ? "小于3" : "other";})
                        )
                );
        System.out.println(categoryMap3);
    }

}
