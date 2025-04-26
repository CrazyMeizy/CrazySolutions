package markup;

import java.util.List;

public class Paragraph implements MarkupElement, BBCodeElement{

    private List<ParagraphElement> list;
    public Paragraph(List<ParagraphElement> list){
        this.list = list;
    }
    @Override
    public void toMarkdown(StringBuilder sb){
        for(ParagraphElement i : this.list){
            i.toMarkdown(sb);
        }
    }
    @Override
    public void toBBCode(StringBuilder sb){
        for(ParagraphElement i : this.list){
            i.toBBCode(sb);
        }
    }
}
