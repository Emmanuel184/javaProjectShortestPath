public class myStack<E> {
    myLinkedList<E> stack;

    myStack() {
        this.stack = new myLinkedList<>();
    }

    public void push(E element) {
        stack.addAtHead(element);
    }

    public boolean isEmpty() {
        return stack.getLength() == 0;
    }

    public E peek() {
        return stack.getHead().element;
    }

    public E pop(){

        if (stack.getLength() == 0) {
            System.out.println("Stack is empty!");
            return null;
        }

        return stack.remove(0);
    }




}
