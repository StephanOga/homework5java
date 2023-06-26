// Задание
// Реализуйте структуру телефонной книги с помощью HashMap.
// Программа также должна учитывать, что во входной структуре будут повторяющиеся имена с разными телефонами,
// их необходимо считать, как одного человека с разными телефонами.
// Вывод должен быть отсортирован по убыванию числа телефонов.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Добро пожаловать! Телефонная книга к вашим услугам. Выбирайте опции");
        while (true) {
            System.out.println("______________________________\n!!!МЕНЮ!!!\n1)Ввести данные абонента (нового или существующего)" +
                    "\n2)Увидеть список абонентов, отсортированный по убыванию числа телефонов" +
                    "\n3)Выход");
            Scanner scanner = new Scanner(System.in);
            int choice = parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    HashMap<String, String> subscriber = new HashMap<>();
                    System.out.print("Давайте добавим данные по новому или существующему абоненту. Введите фамилию: ");
                    String surname = scanner.nextLine();
                    subscriber.put("фамилия", surname);
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();
                    subscriber.put("имя", name);
                    System.out.print("Введите отчество: ");
                    String middleName = scanner.nextLine();
                    subscriber.put("отчество", middleName);
                    System.out.println("Укажите число номеров телефона, которые вы собираетесь ввести: ");
                    int amount = parseInt(scanner.nextLine());
                    ArrayList<String> phonenumbers = new ArrayList<>();
                    for (int i = 0; i < amount; i++) {
                        System.out.println("Введите номер телефона: ");
                        String number = scanner.nextLine();
                        phonenumbers.add(number);
                    }
                    subscriber.put("номера телефонов", String.valueOf(phonenumbers));
                    try (FileWriter fw = new FileWriter("phonebook.txt", true)) {
                        fw.write(subscriber.toString());
                        fw.append("\n");
                        fw.flush();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    System.out.println("Данные успешно внесены");
                    break;
                case 2:
                    System.out.println("Представляю данные об абонентах (ФИО, телефоны): ");
                    BufferedReader br = new BufferedReader(new FileReader("phonebook.txt"));
                    String str;
                    HashMap<String, ArrayList<Long>> superMap = new HashMap<>();
                    while ((str = br.readLine()) != null) {
                        if (!str.isEmpty()) {
                            HashMap<String, String> subscriberFromFile = new HashMap<>();
                            ArrayList<Long> numbers = new ArrayList<>();
                            str = str.replaceAll("[{}]", " ").replaceAll("\\s{2,}", " ");
                            String[] strings = str.split(", ");
                            String result = "";
                            for (int i = 0; i < strings.length; i++) {
                                if (strings[i].contains("фамилия")) subscriberFromFile.put("фамилия", strings[i]
                                        .replaceAll("фамилия=", ""));
                                if (strings[i].contains("имя")) subscriberFromFile.put("имя", strings[i]
                                        .replaceAll("имя=", ""));
                                if (strings[i].contains("отчество")) subscriberFromFile.put("отчество", strings[i]
                                        .replaceAll("отчество=", ""));
                                if (strings[i].contains("номера телефонов=[") || (strings[i].contains("]")) || (strings[i].contains("["))) {
                                    numbers.add(parseLong(strings[i].replaceAll("номера телефонов=\\[|[\\[|\\]]", "").strip()));
                                }
                                if (strings[i].matches("\\d+")) numbers.add(parseLong(strings[i]));
                            }

                            result += subscriberFromFile.get("фамилия") + " " + subscriberFromFile.get("имя") + " " + subscriberFromFile.get("отчество");

                            if (superMap.containsKey(result)) {
                                ArrayList<Long> oldNumbers = superMap.get(result);
                                ArrayList<Long> oldAndNewNumbers = new ArrayList<>(oldNumbers.size() + numbers.size());
                                oldAndNewNumbers.addAll(oldNumbers);
                                oldAndNewNumbers.addAll(numbers);
                                superMap.put(result, oldAndNewNumbers);
                            } else {
                                superMap.put(result, numbers);
                            }
                        }
                    }

                    int i = 0;

                    ArrayList<String[]> arrayList = new ArrayList<>();

                    for (Map.Entry<String, ArrayList<Long>> entry : superMap.entrySet()) {
                        ArrayList<String> helpArrayList = new ArrayList<>();
                        String s1 = entry.getKey();
                        String s2 = entry.getValue().toString();
                        helpArrayList.add(s1);
                        helpArrayList.add(s2);
                        arrayList.add(helpArrayList.toArray(new String[i]));
                        i++;
                    }

                    SortBySize(arrayList);

                    br.close();
                    break;

                case 3:
                    System.out.println("Спасибо за работу с телефонной книгой!");
                    scanner.close();
                    exit(0);
            }
        }
    }

    public static void SortBySize(ArrayList<String[]> myArrayList) {
        myArrayList.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o2[1].length() - o1[1].length();
            }
        });
        for (String[] mystr : myArrayList) {
            System.out.println(mystr[0] + mystr[1]);
        }
    }

}