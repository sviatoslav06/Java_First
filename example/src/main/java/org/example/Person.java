package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Залежність яка дає змогу автоматичного створення гетерів та сетерів
@AllArgsConstructor //Залежність яка автоматично створює звичайний конструктор
@NoArgsConstructor //Залежність яка автоматично створює параметризований конструктор
public class Person implements Comparable {
    private String firstName;
    private String lastName;

    @Override //Перегрузка функції для порівняння
    public int compareTo(Object o) {
        Person p = (Person)o;
        int result = this.lastName.compareTo(p.lastName);
        if(result==0)
            result = this.firstName.compareTo(p.firstName);
        return result;
    }
}
