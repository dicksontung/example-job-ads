'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const axios = require('axios');
const UserDropDown = require('./user_dropdown');

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      users: [],
      products: [],
      chosenUser: 1,
      checkouts: []
    };
    this.chooseUserFn = this.chooseUserFn.bind(this);
    this.getCheckouts = this.getCheckouts.bind(this)
  }

  componentDidMount() {
    axios.get('/api/users').then(resp => {
          this.setState({users: resp.data._embedded.users})
        }
    );
    axios.get('/api/products').then(resp => {
          this.setState({products: resp.data._embedded.products})
        }
    )
  }

  getCheckouts() {
    axios.get('/api/user/' + this.state.chosenUser + '/checkouts').then(
        resp => {
          this.setState({checkouts: resp.data})
        }
    )
  }

  chooseUserFn(userId) {
    console.log(userId);
    this.setState({
      chosenUser: userId
    }, this.getCheckouts)
  }

  render() {
    return (
        <div>
          <UserDropDown users={this.state.users}
                        chooseUserFn={this.chooseUserFn}/>
          <br/>
          <br/>
          <CheckoutList checkouts={this.state.checkouts}
                        products={this.state.products}
                        getCheckouts={this.getCheckouts}/>
          <NewCheckoutButton chosenUser={this.state.chosenUser}
                             getCheckouts={this.getCheckouts}/>
        </div>
    )
  }
}

class CheckoutList extends React.Component {
  render() {
    var checkouts = this.props.checkouts.map(checkout =>
        <Checkout key={checkout.id} checkout={checkout}
                  products={this.props.products}
                  getCheckouts={this.props.getCheckouts}/>
    );
    return (
        <div>
          <h3>Checkouts</h3>
          {checkouts}
        </div>
    )
  }
}

class Checkout extends React.Component {
  render() {
    return (
        <div>
          <pre>{JSON.stringify(this.props.checkout, null, 2)}</pre>
          <NewItemButton products={this.props.products}
                         checkout={this.props.checkout}
                         getCheckouts={this.props.getCheckouts}/>
          <DeleteCheckoutButton checkout={this.props.checkout}
                                getCheckouts={this.props.getCheckouts}/>
          <hr/>
        </div>
    )
  }
}

class ProductOption extends React.Component {
  render() {
    return (
        <option value={this.props.product.id}>
          {this.props.product.name} - {this.props.product.price}
        </option>
    )
  }
}

class NewItemButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      product: {
        id: "Classic"
      }
    };
    this.handleSubmit = this.handleSubmit.bind(this)
    this.change = this.change.bind(this)
  }

  change(event) {
    this.state.product = {
      id: event.target.value
    }
  }

  handleSubmit(event) {
    event.preventDefault();
    var item = {
      product: this.state.product
    };
    axios.post('/api/checkout/' + this.props.checkout.id + '/items', item)
    .then(this.props.getCheckouts)
  }

  render() {
    var products = this.props.products.map(prod =>
        <ProductOption key={prod.id} product={prod}/>
    );
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
            <select onChange={this.change}>
              {products}
            </select>
            <button type="submit">Add new item</button>
          </form>
        </div>
    )
  }
}

class NewCheckoutButton extends React.Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit(event) {
    event.preventDefault();
    var checkout = {
      userId: this.props.chosenUser
    };
    axios.post(`/api/checkout`, checkout)
    .then(this.props.getCheckouts)
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
            <button type="submit">Add new checkout</button>
          </form>
        </div>
    )
  }
}

class DeleteCheckoutButton extends React.Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleSubmit(event) {
    event.preventDefault();
    axios.delete('/api/checkouts/' + this.props.checkout.id)
    .then(this.props.getCheckouts)
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
            <button type="submit">Delete checkout</button>
          </form>
        </div>
    )
  }
}

ReactDOM.render(
    <App/>
    ,
    document.getElementById('react')
);

