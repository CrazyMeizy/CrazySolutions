package markup;

import java.util.List;

public class Emphasis extends AbstractElement{
    public Emphasis(List<ParagraphElement> list){
        super(list);
    }
    //@Override
    //public String markId() {
    //    return "*";
    //}
    @Override
    public void markId(StringBuilder sb) {
        sb.append("*");
    }
    @Override
    public void markOpen(StringBuilder sb) {
        sb.append("[i]");
    }
    @Override
    public void markClose(StringBuilder sb){ sb.append("[/i]");}
}
