package com.techdegree.nikiforo;

    import java.util.Random;

    public class Jar {
        private String mTypeOfItems;

        public String getTypeOfItems() {
            return mTypeOfItems;
        }

        public int getCapacity() {
            return mCapacity;
        }

        private int mCapacity;

        public int getNumberOfItems() {
            return mNumberOfItems;
        }

        protected void setNumberOfItems(int numberOfItems) {
            mNumberOfItems = numberOfItems;
        }

        private int mNumberOfItems;

        public Jar(String typeOfItems, int capacity) {
            mTypeOfItems = typeOfItems;
            mCapacity = capacity;
            Random random = new Random();
            mNumberOfItems = random.nextInt(mCapacity) + 1;
        }
    }
