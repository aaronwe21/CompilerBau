package visitors.visitor1;

import visitors.*;
import nodes.*;

/**
 * @author 8989700
 */

public class Visitor1 implements Visitor {
    private int nextPosition;

    public Visitor1(){
        this.nextPosition=1;
    }

    public void visit(OperandNode node)
    {
        // Zunächst wird dem Operanden die Posotion zugewiesen
        node.position=nextPosition;

        // Handelt es sich um ein Epsilon, wird nullable auf true gesetzt, first- und lastpos bleibt leer
        if(node.symbol.equals("epsilon")){
            // Nullable
            node.nullable= true;
        }
        // Ansonsten wird nullable auf false und first- und lastpos auf die Position des Operanden gesetzt
        else{
            // Nullable
            node.nullable= false;
            // Firstpos
            node.firstpos.add(nextPosition);
            // Lastpos
            node.lastpos.add(nextPosition);
        }
        // Zähler für die Position der Operanden um 1 erhöhen
        nextPosition++;
    }

    public void visit(BinOpNode node)
    {
        // Verhalten für Konkatenation
        if(node.operator.equals("°")){
            // Nullable
            node.nullable=((((SyntaxNode)node.left).nullable)&&(((SyntaxNode)node.right).nullable));

            // Firstpos
            if(((SyntaxNode)node.left).nullable){
                node.firstpos.addAll(((SyntaxNode) node.left).firstpos);
                node.firstpos.addAll(((SyntaxNode) node.right).firstpos);
            }
            else{
                node.firstpos.addAll(((SyntaxNode) node.left).firstpos);
            }

            // Lastpos
            if(((SyntaxNode)node.right).nullable){
                node.lastpos.addAll(((SyntaxNode) node.left).lastpos);
                node.lastpos.addAll(((SyntaxNode) node.right).lastpos);
            }
            else{
                node.lastpos.addAll(((SyntaxNode) node.right).lastpos);
            }
        }
        // Verhalten für Alternative
        else if(node.operator.equals("|"))
        {
            // Nullable
            node.nullable=((((SyntaxNode)node.left).nullable)||(((SyntaxNode)node.right).nullable));

            // Firstpos
            node.firstpos.addAll(((SyntaxNode) node.left).firstpos);
            node.firstpos.addAll(((SyntaxNode) node.right).firstpos);

            // Lastpos
            node.lastpos.addAll(((SyntaxNode) node.left).lastpos);
            node.lastpos.addAll(((SyntaxNode) node.right).lastpos);
        }
        else{
            throw new RuntimeException("Invalid Operator! BinOpNodes support only Concatination ° or Or-Operation |");
        }
    }

    public void visit(UnaryOpNode node)
    {
        // Verhalten für Kleenesche Hülle
        if(node.operator.equals("*")){
            // Nullable
            node.nullable=true;

            // Firstpos
            node.firstpos.addAll(((SyntaxNode)node.subNode).firstpos);

            // Lastpos
            node.lastpos.addAll(((SyntaxNode)node.subNode).lastpos);
        }
        // Verhalten für Positive Hülle
        else if(node.operator.equals("+"))
        {
            // Nullable
            node.nullable=(((SyntaxNode)node.subNode).nullable);

            // Firstpos
            node.firstpos.addAll(((SyntaxNode)node.subNode).firstpos);

            // Lastpos
            node.lastpos.addAll(((SyntaxNode)node.subNode).lastpos);
        }
        // Verhalten für Option
        else if(node.operator.equals("?"))
        {
            // Nullable
            node.nullable=true;

            // Firstpos
            node.firstpos.addAll(((SyntaxNode)node.subNode).firstpos);

            // Lastpos
            node.lastpos.addAll(((SyntaxNode)node.subNode).lastpos);
        }
        else{
            throw new RuntimeException("Invalid Operator! UnaryOpNodes support only Kleene closure *, Positive closure + or option ?");
        }
    }

    // Optionale Methode zum Durchlaufen eines Baumes
    public void visitTree(Visitable root){
        DepthFirstIterator.traverse(root, this);
    }
}
