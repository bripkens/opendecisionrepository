package nl.rug.search.odr.service;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ParagraphImpl implements Paragraph {

    private final String name, content;




    public ParagraphImpl(String name, String content) {
        this.name = name;
        this.content = content;
    }




    @Override
    public String getName() {
        return name;
    }




    @Override
    public String getContent() {
        return content;
    }
}
