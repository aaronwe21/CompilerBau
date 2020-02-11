import nodes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import visitors.DepthFirstIterator;
import visitors.visitor1.Visitor1;

/**
 * @author 8989700
 */

public class Visitor1Test {

    @Test
    void runTestWithTree1()                                 // (a|b)*abb#
    {
        Visitable rootBasic = generateBasicTree1();
        Visitable rootTarget = generateExpectedTree1();

        DepthFirstIterator.traverse(rootBasic, new Visitor1());
        Assertions.assertEquals(equals(rootTarget,rootBasic),true);
    }

    @Test
    void runTestWithTree1OptionalMethod()                                 // (a|b)*abb#
    {
        Visitable rootBasic = generateBasicTree1();
        Visitable rootTarget = generateExpectedTree1();

        new Visitor1().visitTree(rootBasic);
        Assertions.assertEquals(equals(rootTarget,rootBasic),true);
    }

    @Test
    void runTestWithTree2()                                 // a(a|c)b+(c|d)*a
    {
        Visitable rootBasic = generateBasicTree2();
        Visitable rootTarget = generateExpectedTree2();

        DepthFirstIterator.traverse(rootBasic, new Visitor1());
        Assertions.assertEquals(equals(rootTarget,rootBasic),true);
    }


    // EqualsMethode zur Gleichheitsprüfung der Bäume
    private boolean equals(Visitable expected, Visitable visited) {
        if (expected == null && visited == null) return true;
        if (expected == null || visited == null) return false;
        if (expected.getClass() != visited.getClass()) return false;
        if (expected.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) expected;
            BinOpNode op2 = (BinOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }
        if (expected.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) expected;
            UnaryOpNode op2 = (UnaryOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.subNode, op2.subNode);
        }
        if (expected.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) expected;
            OperandNode op2 = (OperandNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos);
        }
        throw new IllegalStateException(
                String.format("Beide Wurzelknoten sind Instanzen der Klasse %1$s !"
                                + " Dies ist nicht erlaubt!",
                        expected.getClass().getSimpleName())
        );
    }

    // Methoden zur manuellen Erzeugung der Beispiel-Basisbäume bzw. der Beispiel-Lösungsbäume
    private Visitable generateBasicTree1()   // (a|b)*abb#
    {
        Visitable or=new BinOpNode("|", new OperandNode("a"), new OperandNode("b"));
        Visitable conc1 = new BinOpNode("°", new UnaryOpNode("*",or),new OperandNode("a"));
        Visitable conc2 = new BinOpNode("°", conc1, new OperandNode("b"));
        Visitable conc3 = new BinOpNode("°", conc2, new OperandNode("b"));
        Visitable conc4 = new BinOpNode("°", conc3, new OperandNode("#"));
        return conc4;
    }

    private Visitable generateExpectedTree1()    // (a|b)*abb#
    {
        OperandNode op1 = new OperandNode("a");
        op1.position=1;
        op1.firstpos.add(1);
        op1.lastpos.add(1);
        op1.nullable=false;
        OperandNode op2 = new OperandNode("b");
        op2.position=2;
        op2.firstpos.add(2);
        op2.lastpos.add(2);
        op2.nullable=false;
        OperandNode op3 = new OperandNode("a");
        op3.position=3;
        op3.firstpos.add(3);
        op3.lastpos.add(3);
        op3.nullable=false;
        OperandNode op4 = new OperandNode("b");
        op4.position=4;
        op4.firstpos.add(4);
        op4.lastpos.add(4);
        op4.nullable=false;
        OperandNode op5 = new OperandNode("b");
        op5.position=5;
        op5.firstpos.add(5);
        op5.lastpos.add(5);
        op5.nullable=false;
        OperandNode op6 = new OperandNode("#");
        op6.position=6;
        op6.firstpos.add(6);
        op6.lastpos.add(6);
        op6.nullable=false;

        Visitable or=new BinOpNode("|",op1, op2);
        ((BinOpNode)or).firstpos.add(1);
        ((BinOpNode)or).firstpos.add(2);
        ((BinOpNode)or).lastpos.add(1);
        ((BinOpNode)or).lastpos.add(2);
        ((BinOpNode)or).nullable=false;
        UnaryOpNode star = new UnaryOpNode("*",or);
        star.firstpos.add(1);
        star.firstpos.add(2);
        star.lastpos.add(1);
        star.lastpos.add(2);
        star.nullable=true;
        Visitable conc1 = new BinOpNode("°", star,op3);
        ((BinOpNode)conc1).firstpos.add(1);
        ((BinOpNode)conc1).firstpos.add(2);
        ((BinOpNode)conc1).firstpos.add(3);
        ((BinOpNode)conc1).lastpos.add(3);
        ((BinOpNode)conc1).nullable = false;
        Visitable conc2 = new BinOpNode("°", conc1,op4);
        ((BinOpNode)conc2).firstpos.add(1);
        ((BinOpNode)conc2).firstpos.add(2);
        ((BinOpNode)conc2).firstpos.add(3);
        ((BinOpNode)conc2).lastpos.add(4);
        ((BinOpNode)conc2).nullable = false;
        Visitable conc3 = new BinOpNode("°", conc2, op5);
        ((BinOpNode)conc3).firstpos.add(1);
        ((BinOpNode)conc3).firstpos.add(2);
        ((BinOpNode)conc3).firstpos.add(3);
        ((BinOpNode)conc3).lastpos.add(5);
        ((BinOpNode)conc3).nullable = false;
        Visitable conc4 = new BinOpNode("°", conc3, op6);
        ((BinOpNode)conc4).firstpos.add(1);
        ((BinOpNode)conc4).firstpos.add(2);
        ((BinOpNode)conc4).firstpos.add(3);
        ((BinOpNode)conc4).lastpos.add(6);
        ((BinOpNode)conc4).nullable = false;

        return conc4;
    }

    // Sample Tree 2
    private Visitable generateBasicTree2()      // a(a|c)b+(c|d)*a
    {
        OperandNode op1 = new OperandNode("a");
        OperandNode op2 = new OperandNode("a");
        OperandNode op3 = new OperandNode("b");
        OperandNode op4 = new OperandNode("b");
        OperandNode op5 = new OperandNode("c");
        OperandNode op6 = new OperandNode("d");
        OperandNode op7 = new OperandNode("a");
        OperandNode op8 = new OperandNode("#");

        BinOpNode or1= new BinOpNode("|",op2,op3);
        BinOpNode or2= new BinOpNode("|",op5, op6);

        UnaryOpNode star = new UnaryOpNode("*",or2);
        UnaryOpNode plus = new UnaryOpNode("+", op4);

        BinOpNode conc1= new BinOpNode("°",op1, or1);
        BinOpNode conc2= new BinOpNode("°",conc1, plus);
        BinOpNode conc3= new BinOpNode("°",conc2, star);
        BinOpNode conc4= new BinOpNode("°",conc3, op7);
        Visitable conc5= new BinOpNode("°",conc4, op8);

        return conc5;
    }

    private Visitable generateExpectedTree2()    // a(a|c)b+(c|d)*a
    {
        OperandNode op1 = new OperandNode("a");
        op1.position=1;
        op1.firstpos.add(1);
        op1.lastpos.add(1);
        op1.nullable=false;
        OperandNode op2 = new OperandNode("a");
        op2.position=2;
        op2.firstpos.add(2);
        op2.lastpos.add(2);
        op2.nullable=false;
        OperandNode op3 = new OperandNode("b");
        op3.position=3;
        op3.firstpos.add(3);
        op3.lastpos.add(3);
        op3.nullable=false;
        OperandNode op4 = new OperandNode("b");
        op4.position=4;
        op4.firstpos.add(4);
        op4.lastpos.add(4);
        op4.nullable=false;
        OperandNode op5 = new OperandNode("c");
        op5.position=5;
        op5.firstpos.add(5);
        op5.lastpos.add(5);
        op5.nullable=false;
        OperandNode op6 = new OperandNode("d");
        op6.position=6;
        op6.firstpos.add(6);
        op6.lastpos.add(6);
        op6.nullable=false;
        OperandNode op7 = new OperandNode("a");
        op6.position=7;
        op7.firstpos.add(7);
        op7.lastpos.add(7);
        op7.nullable=false;
        OperandNode op8 = new OperandNode("#");
        op8.position=8;
        op8.firstpos.add(8);
        op8.lastpos.add(8);
        op8.nullable=false;

        BinOpNode or1= new BinOpNode("|",op2,op3);
        or1.nullable=false;
        or1.firstpos.add(2);
        or1.firstpos.add(3);
        or1.lastpos.add(2);
        or1.lastpos.add(3);
        BinOpNode or2= new BinOpNode("|",op5, op6);
        or2.nullable=false;
        or2.firstpos.add(5);
        or2.firstpos.add(6);
        or2.lastpos.add(5);
        or2.lastpos.add(6);

        UnaryOpNode star = new UnaryOpNode("*",or2);
        star.nullable= true;
        star.firstpos.add(5);
        star.firstpos.add(6);
        star.lastpos.add(5);
        star.lastpos.add(6);
        UnaryOpNode plus = new UnaryOpNode("+", op4);
        plus.nullable=false;
        plus.firstpos.add(4);
        plus.lastpos.add(4);

        BinOpNode conc1= new BinOpNode("°",op1, or1);
        conc1.nullable=false;
        conc1.firstpos.add(1);
        conc1.lastpos.add(2);
        conc1.lastpos.add(3);
        BinOpNode conc2= new BinOpNode("°",conc1, plus);
        conc2.nullable=false;
        conc2.firstpos.add(1);
        conc2.lastpos.add(4);
        BinOpNode conc3= new BinOpNode("°",conc2, star);
        conc3.nullable=false;
        conc3.firstpos.add(1);
        conc3.lastpos.add(4);
        conc3.lastpos.add(5);
        conc3.lastpos.add(6);
        BinOpNode conc4= new BinOpNode("°",conc3, op7);
        conc4.nullable=false;
        conc4.firstpos.add(1);
        conc4.lastpos.add(7);
        Visitable conc5= new BinOpNode("°",conc4, op8);
        ((SyntaxNode)conc5).nullable=false;
        ((SyntaxNode)conc5).firstpos.add(1);
        ((SyntaxNode)conc5).lastpos.add(8);
        return conc5;
    }
}
