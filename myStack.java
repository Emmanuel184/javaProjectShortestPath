/*
 * Stack class basically justu sse linkedList, and for adding(pushing) elements to stack we will just add to head using linkedlist method
 * and pop will just remove the head, using remove method and 0 index, since passing remove(0) to linkedlist will return head and remove it from linked list
 */

public class myStack<E> {
    myLinkedList<E> stack;

    myStack() {
        this.stack = new myLinkedList<>();
    }

    public void push(E element) {
        stack.addAtHead(element);
    }

    //if size of stack is 0 then its empty
    public boolean isEmpty() {
        return stack.getLength() == 0;
    }

    //this helps to debug, and also helps when doing backtrack checks

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
