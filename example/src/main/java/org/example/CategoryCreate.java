package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Залежність яка дає змогу автоматичного створення гетерів та сетерів
@AllArgsConstructor //Залежність яка автоматично створює звичайний конструктор
@NoArgsConstructor //Залежність яка автоматично створює параметризований конструктор
public class CategoryCreate {
    private String name;
    private String description;
}
