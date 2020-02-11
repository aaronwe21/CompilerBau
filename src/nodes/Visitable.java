package nodes;

import visitors.*;

public interface Visitable {
    void accept(Visitor visitor);
}
