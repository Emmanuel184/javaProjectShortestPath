public class myStack<E> {
    myLinkedList<E> stack;

    myStack() {
        this.stack = new myLinkedList<>();
    }

    public void push(E element) {
        stack.addAtHead(element);
    }

    public E pop(){

        if (stack.length == 0) {
            System.out.println("Stack is empty!");
            return null;
        }

        return stack.remove(0);
    }




}
