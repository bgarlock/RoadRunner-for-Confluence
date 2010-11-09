package test;


import javax.swing.tree.*;

/** This class forces "male" nodes to have leaf icons and
forbids male childbaring ability */
public class PersonNode extends DefaultMutableTreeNode  {

  public PersonNode(PersonalInfo info) {
    super(info);
  }

  /** Override a few methods... */
  public boolean isLeaf() {
    //Note: Male == true;
    return ((PersonalInfo) getUserObject()).isMale();
  }

  public boolean getAllowsChildren() {
    //Note: Male == true;
    return !((PersonalInfo) getUserObject()).isMale();
  }

  public void add(DefaultMutableTreeNode child) {
    super.add(child);
    //System.out.println(child + " added to " + this);

    PersonalInfo childPI = (PersonalInfo) ((PersonNode) child).getUserObject();

    PersonalInfo oldParent = childPI.getParent();
    //if (parent != null) oldParent.remove(childPI);

    PersonalInfo newParent = (PersonalInfo) getUserObject();

    newParent.add(childPI);
  }

  public void remove(DefaultMutableTreeNode child) {
    super.remove(child);
    //System.out.println(child + " removed from " + this);

    PersonalInfo childPI = (PersonalInfo) ((PersonNode) child).getUserObject();

    PersonalInfo ParentPI = (PersonalInfo) getUserObject();
    if (parent != null) ParentPI.remove(childPI);
  }
}
