package markup;

import java.util.List;

public class Strikeout extends AbstractElement{
    private List<ParagraphElement> list;
    public Strikeout(List<ParagraphElement> list){
        super(list);
    }
    //можно реализовать через return
    @Override
    public void markId(StringBuilder sb){
        sb.append("~");
    }
    @Override
    public void markOpen(StringBuilder sb){ sb.append("[s]");}
    @Override
    public void markClose(StringBuilder sb){ sb.append("[/s]");}
}
