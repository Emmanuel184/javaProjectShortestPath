import java.util.Iterator;

public class myLinkedList<E> implements Iterable<E>{

    private node<E> head;

    private int length;

    public myLinkedList(E city) {
        this.head = new node<>(city);
        this.length = 0;
    }

    public myLinkedList() {
        this.head = null;
        this.length = 0;
    }

    public int getLength() {
        return this.length;
    }

    public void addAtHead(E elemenet) {

        node<E> newNode = new node<>(elemenet);

        // check if first element in our linked list
        if (head == null) {
            this.head = newNode;
        } else {

            newNode.next = this.head;
            this.head = newNode;
        }

        this.length++;
    }

    public void addAtTail(E element) {

        node<E> newNode = new node<>(element);

        if(this.head == null) {
            this.head = newNode;
        } else {
            node<E> currentNode = this.head;
            
            while(currentNode.next != null) {
                currentNode = currentNode.next;
            }

            currentNode.next = newNode;
        }

        this.length++;
    }

    public E remove(int index) {

        node<E> previosNode = null;
        node<E> currentNode = this.head;

        if(index < 0 || index > this.length - 1) {
            System.out.println("INDEX OUT OF BOUNDS");
            return null;
        }

        if (index == 0) {
            this.head = currentNode.next;
            

        } else {

            for(int i = 0; i < index; i++) {
                previosNode = currentNode;
                currentNode = currentNode.next;
            }

            previosNode.next = currentNode.next;
        }

        this.length--;

        return currentNode.element;

    }

    public boolean isInList(E element) {

        if (this.head == null) {
            return false;
        }

        node<E> currentNode = this.head;

        while (currentNode.next != null && !(currentNode.element.equals(element))) {
            currentNode = currentNode.next;
        }

        if (currentNode.element.equals(element)) {
            return true;
        } else {
            return false;
        }
    }

    public int getIndex(E element) {

        node<E> newNode = new node<E>(element);
        node<E> currentNode = this.head;

        int returnIndex = 0;

        if(this.head.element.equals(newNode.element)) {
            return 0;
        } else {

            while(currentNode.next != null && !(currentNode.element.equals(element))) {
                currentNode = currentNode.next;
                returnIndex++;
                
            }

        }

        if (currentNode.element.equals(element)) {
            return returnIndex;
        } else {
            return -1;
        }
        
    }

    public E get(E element) {

        if (this.head.element.equals((element))) {
            return this.head.element;
        }

        node<E> returnNode = this.head;

        while (returnNode.next != null && !(returnNode.element.equals(element))) {

            returnNode = returnNode.next;

        }

        if (!(returnNode.element.equals(element))) {
            return null;
        } else {
            return returnNode.element;
        }

    }

    public String toString() {

        String returnString = "";

        node<E> currentNode = this.head;

        while (currentNode.next != null) {
            returnString += currentNode.element + "\n";
            currentNode = currentNode.next;
        }

        return returnString;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        myLinkedList<city> cityLinkedList = (myLinkedList) o;

        return this.head.element.equals(cityLinkedList.head.element);

    }

    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return new linkedListIterable(this);
    }

    public node<E> getHead() {
        return this.head;
    }

}

class node<E> {

    E element;

    node<E> next;

    node(E element) {
        this.element = element;
        this.next = null;
    }

}

class linkedListIterable<E> implements Iterator<E> {

    node<E> currentNode;

    linkedListIterable(myLinkedList<E> linkedList) {
        this.currentNode = linkedList.getHead();
    }

    public boolean hasNext() {
        return currentNode != null;
    }

    public E next() {
        E returnElement = currentNode.element;
        currentNode = currentNode.next;
        return returnElement;
    }

}