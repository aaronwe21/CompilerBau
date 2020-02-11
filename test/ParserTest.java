import nodes.BinOpNode;
import nodes.OperandNode;
import nodes.UnaryOpNode;
import nodes.Visitable;
import org.junit.jupiter.api.Test;
import parser.ParseException;
import parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 4843585
 */
class ParserTest {

    @Test
    void testParserSuccessfully() throws ParseException {
        Parser parser = new Parser();
        String regex = "(a|b)*abb#";
        String regexTrue = regex.substring(0, regex.length() - 1);          // cuts out the #
        assertTrue(parser.checkRegularExpression(regexTrue));

        //hard-coded expectedTree
        BinOpNode or1 = new BinOpNode("|", new OperandNode("a"), new OperandNode("b"));
        UnaryOpNode star1 = new UnaryOpNode("*", or1);
        BinOpNode conc1 = new BinOpNode("°", new OperandNode("b"), new OperandNode("b"));
        BinOpNode conc2 = new BinOpNode("°", new OperandNode("a"), conc1);
        BinOpNode conc3 = new BinOpNode("°", star1, conc2);
        BinOpNode expectedTree = new BinOpNode("°", conc3, new OperandNode("#"));

        //test if expectedTree equals SyntaxTree from Parser
        assertTrue(equals(expectedTree, (BinOpNode)parser.getSyntaxTree(regex)));
    }

    @Test
    void testParserFail() {
        Parser parser = new Parser();

        String[] testStrings = {"", "x", "#", "(((x)#", "x(*m)#", "#asdf#", "äae#", ",#", "(x))#", "(x))((x))", "*x#", "a*+b+#"};

        for (String s: testStrings) {

            try{
                parser.getSyntaxTree(s);
                fail("003: Parser accepted wrong statement");
            }catch (ParseException e){
                assertTrue(true);
                if(!e.getClass().equals(ParseException.class)){
                    fail("004: Wrong exception");
                    e.printStackTrace();
                }
            }
        }

    }

    private static boolean equals(Visitable v1, Visitable v2)
    {
        if (v1 == v2)
            return true;
        if (v1 == null)
            return false;
        if (v2 == null)
            return false;
        if (v1.getClass() != v2.getClass())
            return false;
        if (v1.getClass() == OperandNode.class)
        {
            OperandNode op1 = (OperandNode) v1;
            OperandNode op2 = (OperandNode) v2;
            return op1.position == op2.position && op1.symbol.equals(op2.symbol);
        }
        if (v1.getClass() == UnaryOpNode.class)
        {
            UnaryOpNode op1 = (UnaryOpNode) v1;
            UnaryOpNode op2 = (UnaryOpNode) v2;
            return op1.operator.equals(op2.operator) && equals(op1.subNode, op2.subNode);
        }
        if (v1.getClass() == BinOpNode.class)
        {
            BinOpNode op1 = (BinOpNode) v1;
            BinOpNode op2 = (BinOpNode) v2;
            return op1.operator.equals(op2.operator) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }
        throw new IllegalStateException("Ungueltiger Knotentyp");
    }
}