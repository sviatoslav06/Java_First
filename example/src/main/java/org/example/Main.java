package org.example;

import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        //inputData();
        //simpleArray();
        //sortPerson();

        //jdbc - ado.net
        String userName = "root";
        String password = "";
        String host = "localhost";
        String port = "3306";
        String database = "java_spu111";
        String strConn = "jdbc:mariadb://"+host+":"+port+"/"+database;
        //createCategory(strConn, userName, password);
        //insertCategory(strConn, userName, password);

        int q = 1;
        Scanner input = new Scanner(System.in);
        while (q != 0){
            System.out.println("Що ви хочете зробити?");
            System.out.println("1 - Вивести список категорій");
            System.out.println("2 - Додати категорію");
            System.out.println("3 - Редагувати категорію");
            System.out.println("4 - Видалити категорію");
            System.out.println("0 - Вихід");
            q = input.nextInt();

            switch (q) {
                case 1:
                    System.out.println("Список категорій");
                    var list = listCategories(strConn, userName, password);
                    for (var c : list) {
                        System.out.println("-----" + c.getId() + "-----");
                        System.out.println("Назва: " + c.getName());
                        System.out.println("Опис: " + c.getDescription());
                    }
                    break;
                case 2:
                    insertCategory(strConn, userName, password);
                    break;
                case 3:
                    editCategory(strConn, userName, password);
                    break;
                case 4:
                    deleteCategory(strConn, userName, password);
                    break;
                case 0:
                    System.out.println("До зустрічі!");
                    break;
                default:
                    System.out.println("Щось пішло не так...");
            }
        }
    }

    private static void deleteCategory(String strConn, String userName, String password) {
        Scanner input = new Scanner(System.in);
        System.out.println("Список категорій");
        var list = listCategories(strConn, userName, password);
        for (var c : list) {
            System.out.println("-----" + c.getId() + "-----");
            System.out.println("Назва: " + c.getName());
            System.out.println("Опис: " + c.getDescription());
        }
        int c = 1;
        while (c < list.size() || c > list.size()) {
            System.out.println("Введіть номер категорії яку ви хочете видалити: ");
            c = input.nextInt();
            input.nextLine();
        }
        try(Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            String sql = "DELETE FROM categories WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, c);

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("Видалено рядків: " + rowsAffected);
            System.out.println("Категорія видалена успішно.");
        }
        catch(Exception ex) {
            System.out.println("Помилка видалення категорії: " + ex.getMessage());
        }
    }

    private static void editCategory(String strConn, String userName, String password){
        System.out.println("Список категорій");
        var list = listCategories(strConn, userName, password);
        for (var c : list) {
            System.out.println("-----" + c.getId() + "-----");
            System.out.println("Назва: " + c.getName());
            System.out.println("Опис: " + c.getDescription());
        }
        Scanner input = new Scanner(System.in);
        int c = 1;
        while (c < list.size() || c > list.size()) {
            System.out.println("Введіть номер категорії яку ви хочете відредагувати: ");
            c = input.nextInt();
            input.nextLine();
        }
        input.nextLine();
        try(Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            Statement statement = conn.createStatement();

            CategoryItem categoryToUpdate = new CategoryItem();
            categoryToUpdate.setId(c);
            System.out.println("Вкажіть нову назву категорії: ");
            categoryToUpdate.setName(input.nextLine());
            System.out.println("Вкажіть новий опис категорії: ");
            categoryToUpdate.setDescription(input.nextLine());

            String updateQuery = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);

            preparedStatement.setInt(3, categoryToUpdate.getId());
            preparedStatement.setString(1, categoryToUpdate.getName());
            preparedStatement.setString(2, categoryToUpdate.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();

            System.out.println("Додано рядків: " + rowsAffected);
            System.out.println("Категорія відредагована успішно.");
        }
        catch(Exception ex) {
            System.out.println("Помилка редагування категорії: " + ex.getMessage());
        }
    }

    private static List<CategoryItem> listCategories(String strConn, String userName, String password)
    {
        try(Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            Statement statement = conn.createStatement(); // Створення об'єкта для виконання SQL-запитів
            String selectQuery = "SELECT * FROM categories"; // SQL-запит на вибір всіх категорій
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery); // Створення об'єкта PreparedStatement
            ResultSet resultSet = preparedStatement.executeQuery(); // Виконання SELECT-запиту
            List<CategoryItem> list = new ArrayList<>(); // Створення списку для зберігання об'єктів категорій

            while (resultSet.next()) {
                CategoryItem category = new CategoryItem(); // Створення об'єкта категорії

                // Отримання даних з поточного рядка
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setDescription(resultSet.getString("description"));

                list.add(category); // Додавання об'єкта категорії до списку
            }

            // Закриття ресурсів
            resultSet.close();
            preparedStatement.close();
            return list;
        }
        catch(Exception ex) {
            System.out.println("Помилка читання списку даних: " + ex.getMessage()); // Виведення повідомлення про помилку у разі виникнення виключення
            return null;
        }
    }

    private static void insertCategory(String strConn, String userName, String password) {
        Scanner input = new Scanner(System.in);
        CategoryCreate categoryCreate = new CategoryCreate();
        System.out.println("Вкажіть назву категорії: ");
        categoryCreate.setName(input.nextLine());
        System.out.println("Вкажіть опис категорії: ");
        categoryCreate.setDescription(input.nextLine());

        try(Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            Statement statement = conn.createStatement(); // Створення об'єкта для виконання SQL-запитів

            String insertQuery = "INSERT INTO categories (name, description) VALUES (?, ?)"; // SQL-запит для вставки нової категорії
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery); // Створення об'єкта PreparedStatement

            // Встановлення значень параметрів для SQL-запиту
            preparedStatement.setString(1, categoryCreate.getName());
            preparedStatement.setString(2, categoryCreate.getDescription());

            int rowsAffected = preparedStatement.executeUpdate(); // Виконання SQL-запиту та отримання кількості змінених рядків

            preparedStatement.close();  // Закриття ресурсів

            System.out.println("Додано рядків: " + rowsAffected);
            System.out.println("Категорія створена успішно.");
        }
        catch(Exception ex) {
            System.out.println("Помилка створення категорії: " + ex.getMessage()); // Виведення повідомлення про помилку у разі виникнення виключення
        }
    }

    private static void createCategory(String strConn, String userName, String password) {
        try(Connection conn = DriverManager.getConnection(strConn, userName, password)) {
            System.out.println("Підключення до БД успішно");
            Statement statement = conn.createStatement(); // Створення об'єкта для виконання SQL-запитів

            // SQL-запит для створення таблиці, якщо вона ще не існує
            String createTableSQL = "CREATE TABLE IF NOT EXISTS categories ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "description TEXT"
                    + ")";

            statement.execute(createTableSQL); // Виконання SQL-запиту на створення таблиці

            statement.close(); // Закриття ресурсів

            System.out.println("В БД створено таблицю categories");
        }
        catch (Exception ex) {
            System.out.println("Error connection: " + ex.getMessage()); // Виведення повідомлення про помилку у разі виникнення виключення
        }
    }


    private static void sortPerson(){
        Person[] list = {
                new Person("Іван","Мельник"),
                new Person("Мальвіна","Морква"),
                new Person("Петро","Підкаблучник"),
                new Person("Олег","Гризун")
        };
        System.out.println("-----Звичайний список-----");
        for (var item : list) {
            System.out.println(item);
        }

        Arrays.sort(list);
        System.out.println("-----Посортований список-----");
        for (var item : list) {
            System.out.println(item);
        }
    }

    private static void simpleArray(){
        Scanner scanner = new Scanner(System.in);
        int n = 10;
        int []mas = new int[10];
        for (int i = 0; i < n; i++)
            mas[i] = getRandom(-5, 30);
        System.out.println("-----Початковий масив-----");
        for(int item : mas){
            System.out.printf("%d\t",item);
        }

        System.out.println();

        Arrays.sort(mas);
        System.out.println("-----Відсортований масив-----");
        for(int item : mas){
            System.out.printf("%d\t",item);
        }

        int count  = 0;
        for (int item : mas) {
            if(item >= 0)
                count++;
        }
        System.out.println("\nКількість додатніх чисел - " + count);
    }

    private static int getRandom(int min, int max) {
        // Create an instance of the Random class
        Random random = new Random();
        // Generate a random value between min (inclusive) and max (exclusive)
        return random.nextInt(max - min) + min;
    }

    public static void inputData(){
        Scanner input = new Scanner(System.in);
        System.out.println("Як вас звати?");
        String name = input.nextLine();
        System.out.println("Привіт " + name + "!");
    }
}
