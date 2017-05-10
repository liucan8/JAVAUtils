package com.lc.utils;


import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CollectionUtil {


    /**
     * @param <T>
     * @param set
     * @return list
     */
    public static <T> List<T> set2List(Set<T> set) {
        if (set == null) {
            return null;
        }

        List<T> list = new ArrayList<T>();
        list.addAll(set);
        return list;
    }

    /**
     * @param <T>
     * @param list
     * @return list
     */
    public static <T> Set<T> list2Set(List<T> list) {
        Set<T> set = new HashSet<T>();
        set.addAll(list);
        return set;
    }

    /**
     * @param <T>
     * @param ary
     * @return 将数组转为List
     */
    public static <T> List<T> ary2List(T[] ary) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < ary.length; i++) {
            list.add(ary[i]);
        }
        return list;
    }


    public static void copy(List<Integer> destList, List<Integer> srcList) {
        destList.clear();
        destList.addAll(srcList);
    }

    /**
     * 将double型的整形数据列表转化为long型列表
     *
     * @param doubleIdList
     * @return
     */
    public static List<Long> transferToLongList(List doubleIdList) {
        List<Long> longIdList = new ArrayList<Long>();

        for (Object obj : doubleIdList) {
            if (obj instanceof Double) {
                try {
                    Long tempId = ((Double) obj).longValue();
                    longIdList.add(tempId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (obj instanceof Long) {
                longIdList.add((Long) obj);
            } else if (obj instanceof Integer) {
                longIdList.add(Long.parseLong(obj.toString()));
            }
        }

        return longIdList;
    }

    public static List<Integer> transferToIntegerList(List doubleIdList) {
        List<Integer> intIdList = new ArrayList<Integer>();

        for (Object obj : doubleIdList) {
            if (obj instanceof Double) {
                try {
                    //Cannot cast from Double to Integer
                    Integer tempId = ((Double) obj).intValue();
                    intIdList.add(tempId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (obj instanceof Integer) {
                intIdList.add((Integer) obj);
            } else if (obj instanceof Integer) {
                intIdList.add(Integer.parseInt(obj.toString()));
            }
        }

        return intIdList;
    }

    /**
     * 将两个list中值相同数据删除，剩下不相同的, 返回相同的
     *
     * @param list1
     * @param list2
     */
    public static List<Integer> checkIdentical(List<Integer> list1, List<Integer> list2) {
        List<Integer> resultList = new ArrayList<Integer>();

        Iterator<Integer> iterator1 = list1.iterator();
        while (iterator1.hasNext()) {
            Integer next1 = iterator1.next();
            Iterator<Integer> iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                Integer next2 = iterator2.next();
                if (next1.intValue() == next2.intValue()) {
                    iterator1.remove();
                    iterator2.remove();
                    resultList.add(next1);
                    break;
                }
            }
        }

        return resultList;
    }

    /**
     * added by liyong
     */
    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    /**
     * added by liyong
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * added by liyong
     */
    public static boolean isNotEmpty(Collection c) {
        return !isEmpty(c);
    }

    /**
     * added by liyong
     */
    public static <T> Collection<T> subtract(Collection<T> c1, Collection<T> c2) {
        if (isEmpty(c1))
            return new ArrayList<T>(0);
        if (isEmpty(c2))
            return new ArrayList<T>(c1);

        List<T> result = new ArrayList<T>(c1);
        for (Iterator<T> it = c2.iterator(); it.hasNext(); ) {
            result.remove(it.next());
        }
        return result;
    }

    /**
     * added by liyong
     */
    public static <T> Collection<T> same(Collection<T> c1, Collection<T> c2) {
        if (isEmpty(c1) || isEmpty(c2))
            return new ArrayList<T>(0);

        List<T> result = new ArrayList<T>();
        for (T item : c2) {
            if (c1.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * added by liyong
     */

    public static <T> Collection<T>[] analyse(Collection<T> oldC, Collection<T> newC) {
        return new Collection[]{subtract(newC, oldC), subtract(oldC, newC), same(oldC, newC)};
    }

    public static List<String>[] diff(List<String> list1, List<String> list2) {
        if (list1 == null)
            list1 = new ArrayList<String>();
        if (list2 == null)
            list2 = new ArrayList<String>();

        List<String> toAddList = new ArrayList<String>();
        List<String> toDelList = new ArrayList<String>();

        for (String str1 : list1) {
            if (!list2.contains(str1)) {
                toAddList.add(str1);
            }
        }

        for (String str2 : list2) {
            if (!list1.contains(str2)) {
                toDelList.add(str2);
            }
        }

        return new List[]{toAddList, toDelList};
    }

    public static void deleteDuplicate(List<Integer> list) {
        List<Integer> tempList = new ArrayList<Integer>();

        Iterator<Integer> iter = list.iterator();
        while (iter.hasNext()) {
            Integer current = iter.next();
            if (tempList.contains(current)) {
                iter.remove();
            } else {
                tempList.add(current);
            }
        }
    }
}
