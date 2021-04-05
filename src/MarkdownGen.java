import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllLines;

public class MarkdownGen {
    private static int [] d = new int[6];

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean check_file = true;
        while (check_file) {
            System.out.println("Введите путь к markdown файлу"); // /Users/vladadanilova/Desktop/Veeam.md
            String str = sc.nextLine();
            File input = new File(str);
            if (input.exists() && (getFileExtension(str).equals("md") || getFileExtension(str).equals("markdown")))
            {
                check_file = false;
            }
            else {
                System.out.println("Нет такого markdown файла");
            }
            try {
                FileReader reader = new FileReader(str);
                //BufferedReader read = new BufferedReader(reader);
                /*StringBuilder content = new StringBuilder();
                List<String> lines = readAllLines(Paths.get(str), UTF_8);
                for (String s: lines) {
                        if (s.contains("#") && s.indexOf("#") == 0) {
                            content.append(print(s));
                        }
                }
                content.append("\n"); // отступ после оглавления

                    for (String s: lines) {
                        content.append(s).append("\n");
                    }
                    Files.write(Paths.get(str), Collections.singleton(content));*/

            } catch (FileNotFoundException e) {
                System.out.println("Ошибка - FileNotFoundException. Не удалось найти файл");
            }

            StringBuilder content = new StringBuilder();
            List<String> lines = null;
            try {
                lines = readAllLines(Paths.get(str), UTF_8);
            } catch (IOException e) {
                System.out.println("Ошибка - IOException. Не удалось считать данные из файла");
            }
            for (String s: lines) {
                if (s.contains("#") && s.indexOf("#") == 0) {
                    content.append(print(s));
                }
            }
            content.append("\n"); // отступ после оглавления

            for (String s: lines) {
                content.append(s).append("\n");
            }
            try {
                Files.write(Paths.get(str), Collections.singleton(content));
            } catch (IOException e) {
                System.out.println("Ошибка - IOException. Не удалось обновить файл. Проверьте права доступа");
            }
            System.out.println(content);


        }
        sc.close();



    }

    //метод определения расширения файла
    private static String getFileExtension (String fileName) {
        // если в имени файла есть точка и она не является первым символом в названии файла
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".") + 1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }
    //метод подсчета знаков #
    private static int count (String str) {
        int ans = 1;
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == '#')
            {
                ans++;
            }
            else
                break;
        }
        return ans;
    }
    //метод для создания оглавления и вывода его
    private static String print(String str) {
        int s = count(str);
        String ans = "";
        d[s-1]++;
        String p = "";
        for (int i = 0; i < s - 1; i++)
        {
            p = p + "   ";
        }
        ans = p + d[s - 1] + ". " + "[" + edit(str) + "](#" + link(str) + ") \n";
        for (int i = s; i < d.length; i++)
        {
            d[i] = 0;
        }
        return ans;
    }

    //методы редактирования отображения строки
    private static String edit (String str) {
       StringBuilder edited = new StringBuilder(str);
        for (int i = 0; i < edited.length(); i++) {
            if (edited.charAt(i) == '#')
            {
                edited.deleteCharAt(i);
                i = -1;
            }
            else if (edited.charAt(i) != '#') {
                break;
            }
        }
        return String.valueOf(edited);
    }

    private static String link (String str) {
        return edit(str).replace(' ', '-');
    }

}
