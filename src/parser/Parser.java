package parser;

import nodes.*;


/**
 * @author 4843585
 */
public class Parser {
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final String logicalOperators = "|°";
    private final String repetitiveOperators = "*+?";
    private final String operators = logicalOperators + repetitiveOperators;
    private final String symbols = alphabet + operators + "()";

    public Visitable getSyntaxTree(String expression) throws ParseException {
        if (expression.equals("#") || expression.isEmpty())
            throw new ParseException("1001: Wrong expression");
        if (expression.charAt(expression.length() - 1) != '#')
            throw new ParseException("1002: Expression has to have a '#' in the end");
        expression = expression.substring(0, expression.length() - 1);   // cuts out the #
        expression = expression.replaceAll("\\s", "");     // removes all kinds of spaces
        if (!checkRegularExpression(expression))
            throw new ParseException("1003: Statement contains wrong symbols/ a wrong constellations '" + expression + "'");
        expression = addKonkatenation(expression);

        //Adding root and # to the tree
        Visitable temp = buildTree(expression);
        return new BinOpNode("°", temp, new OperandNode("#"));
    }

    private Visitable buildTree(String expression) throws ParseException {
        if (expression.isEmpty()) throw new ParseException("1004: Empty expression");

        String[] splitExpression = splitExpression(expression);
        for (String s : splitExpression) {
            if (s.isEmpty()) throw new ParseException("1005: Empty expression");
        }
        if (splitExpression.length == 1) return new OperandNode(splitExpression[0]);
        if (splitExpression.length == 2) {
            return new UnaryOpNode(splitExpression[0], buildTree(splitExpression[1]));
        } else { //case length == 3
            return new BinOpNode(splitExpression[0], buildTree(splitExpression[1]), buildTree(splitExpression[2]));
        }

    }

    private String[] splitExpression(String expression) throws ParseException {
        expression = eraseBrackets(expression);

        if (expression.length() == 1) {
            if (alphabet.contains(String.valueOf(expression.charAt(0)))) return new String[]{expression};
            throw new ParseException("1006: Wrong Symbol for nodes.OperandNode '" + expression.charAt(0) + "'");
        }

        int split = findSplit(expression);
        if (split == -1) throw new ParseException("1007: Wrong Statement '" + expression + "'");

        if (replacement(expression.charAt(split)) < 2)
            return new String[]{expression.substring(split, split + 1), expression.substring(0, split), expression.substring(split + 1)};
        return new String[]{expression.substring(split, split + 1), expression.substring(0, split)};
    }


    public boolean checkRegularExpression(String expression) throws ParseException {

        char currentSymbol;
        int brackets = 0;

        // checks first and last symbol
        if ((!alphabet.contains(String.valueOf(expression.charAt(0))) && expression.charAt(0) != '(') ||
                logicalOperators.contains(String.valueOf(expression.charAt(expression.length() - 1))) ||
                expression.charAt(expression.length() - 1) == '(') return false;

        for (int x = 0; x < expression.length(); x++) {
            currentSymbol = expression.charAt(x);

            //zaehlt und prueft Klammern
            if (currentSymbol == '(') brackets++;
            if (currentSymbol == ')') brackets--;
            if (brackets < 0) throw new ParseException("1008: Brackets are wrong");

            //prueft ob Zeichen zur Sprache gehoert
            if (!symbols.contains(String.valueOf(currentSymbol)))
                throw new ParseException("1009 Symbol '" + currentSymbol + "' is not allowed");
        }

        for (int x = 1; x < expression.length() - 1; x++) {
            if (repetitiveOperators.contains(expression.charAt(x) + "") && repetitiveOperators.contains(expression.charAt(x + 1) + ""))
                throw new ParseException("1010: '" + expression.substring(x - 1, x + 2) + "' is not allowed you have to use brackets between");
        }

        if (brackets != 0) throw new ParseException("1010: You forgot to close some brackets");

        return true;
    }

    private String addKonkatenation(String expression) {
        for (int x = 0; x < expression.length() - 1; x++) {
            if (checkFirstOperation(expression.charAt(x)) && checkSecondOperation(expression.charAt(x + 1)))
                expression = expression.substring(0, x + 1) + "°" + expression.substring(x + 1);
        }

        return expression;
    }

    private boolean checkFirstOperation(char symbol) {
        if (alphabet.contains(String.valueOf(symbol))) return true;
        if (repetitiveOperators.contains((String.valueOf(symbol)))) return true;
        return symbol == ')';
    }

    private boolean checkSecondOperation(char symbol) {
        if (alphabet.contains(String.valueOf(symbol))) return true;
        return symbol == '(';
    }

    private int findSplit(String expression) {
        //sucht schwaechsten Operator
        int brackets = 0;
        char currentChar;
        char lowestSymbol = '#'; // # is a spaceholder
        int positionLowestSymbol = -1;


        for (int x = 0; x < expression.length(); x++) {
            currentChar = expression.charAt(x);
            if (currentChar == '(') {
                brackets++;
                continue;
            }
            if (currentChar == ')') {
                brackets--;
                continue;
            }
            if (brackets == 0 && operators.contains(String.valueOf(currentChar))) {
                if (checkLower(lowestSymbol, currentChar)) {
                    lowestSymbol = currentChar;
                    positionLowestSymbol = x;
                }
            }
        }

        return positionLowestSymbol;
    }

    private boolean checkLower(char current, char check) {
        if (current == '#') return true;

        int intCurrent = replacement(current);
        int intCheck = replacement(check);

        return intCurrent > intCheck;
    }

    private int replacement(char toReplace) {
        switch (toReplace) {
            case '*':
            case '+':
            case '?':
                return 2;
            case '|':
                return 0;
            default:
                return 1;
        }

    }

    private String eraseBrackets(String expression) {
        while (!expression.isEmpty() && expression.charAt(0) == '(' &&
                expression.charAt(expression.length() - 1) == ')') {
            boolean erasePossible = true;
            int bracketsCount = 0;
            for (int x = 0; x < expression.length() && erasePossible; x++) {
                if (expression.charAt(x) == '(') bracketsCount++;
                if (expression.charAt(x) == ')') bracketsCount--;
                if (x > 0 && x < expression.length() - 1 && bracketsCount == 0) erasePossible = false;
            }

            if (!erasePossible) break;
            expression = expression.substring(1, expression.length() - 1);
        }

        return expression;
    }

}
