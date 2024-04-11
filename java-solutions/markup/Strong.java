package markup;

import java.util.List;

public class Strong extends AbstractElement{
    private List<ParagraphElement> list;
    public Strong(List<ParagraphElement> list){
        super(list);
    }
    @Override
    public void markId(StringBuilder sb){
        sb.append("__");
    }
    @Override
    public void markOpen(StringBuilder sb){ sb.append("[b]");}
    @Override
    public void markClose(StringBuilder sb){ sb.append("[/b]");}
}
