package lecture11;

class CartPt {
  int x;
  int y;

  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  boolean samePoint(CartPt that) {
    return this.x == that.x && this.y == that.y;
  }
}

