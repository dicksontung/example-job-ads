const React = require('react');

class ProductList extends React.Component {
  render() {
    var products = this.props.products.map(product =>
        <Product key={product.id} product={product}/>
    );
    return (
        <table>
          <tbody>
          <tr>
            <th>id</th>
            <th>name</th>
            <th>price</th>
          </tr>
          {products}
          </tbody>
        </table>
    )
  }
}

class Product extends React.Component {
  render() {
    return (
        <tr>
          <td>{this.props.product.id}</td>
          <td>{this.props.product.name}</td>
          <td>{this.props.product.price}</td>
        </tr>
    )
  }
}

module.exports = ProductList;