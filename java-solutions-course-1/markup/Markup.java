package markup;

import java.util.List;

public class Markup {
    public static void main(String[] args) {

        Paragraph paragraph = new Paragraph(List.of(
                new Strong(List.of(
                        new Text("1"),
                        new Strikeout(List.of(
                                new Text("2"),
                                new Emphasis(List.of(
                                        new Text("3"),
                                        new Text("4")
                                )),
                                new Text("5")
                        )),
                        new Text("6")
                ))
        ));

        StringBuilder markdown = new StringBuilder();
        paragraph.toMarkdown(markdown);
        System.out.println(markdown.toString()); // Вывод: __1~2*34*5~6__


        /*
        Strikeout emphasis = new Strikeout(List.of(new Strong(List.of(new Emphasis(List.of(new Text("123"), new Text("567"))),new Text("123"))),new Text("432")));
        StringBuilder markdown = new StringBuilder();
        emphasis.toMarkdown(markdown);
        System.out.println(markdown.toString());

         */
    }

}
