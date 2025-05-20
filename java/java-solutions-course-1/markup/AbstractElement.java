package markup;

import java.util.List;


public abstract class AbstractElement implements ParagraphElement{
    private List<ParagraphElement> list;
    public AbstractElement(List<ParagraphElement> list){
        this.list = list;
    }
    @Override
    public void toMarkdown(StringBuilder sb){
        //sb.append(markId());
        markId(sb);
        for(ParagraphElement i : this.list){
            i.toMarkdown(sb);
        }
        markId(sb);
    }
    @Override
    public void toBBCode(StringBuilder sb){
        markOpen(sb);
        for(ParagraphElement i : this.list){
            i.toBBCode(sb);
        }
        markClose(sb);
    }
    //protected abstract String markClose();
    protected abstract void markClose(StringBuilder sb);
    protected abstract void markOpen(StringBuilder sb);

    protected abstract void markId(StringBuilder sb);
}
