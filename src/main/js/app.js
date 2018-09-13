'use strict';
const React = require('react');
const ReactDOM = require('react-dom');
const axios = require('axios')

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      users: [],
      products: [],
      checkout: {
        userId: ""
      }
    };
    this.chooseUserFn = this.chooseUserFn.bind(this)
  }

  componentDidMount() {
    axios.get('/api/users').then(resp => {
          this.setState({users: resp.data._embedded.users})
        }
    )
    axios.get('/api/products').then(resp => {
          this.setState({products: resp.data._embedded.products})
        }
    )
  }

  chooseUserFn(userId) {
    this.state.checkout.userId = userId
  }

  render() {
    return (
        <div>
          <UserDropDown users={this.state.users}
                        chooseUserFn={this.chooseUserFn}/>
          <pre>
            {JSON.stringify(this.state.products, null, 2)}
          </pre>
          <CheckoutList checkout={this.state.checkout}/>
        </div>
    )
  }
}

class UserDropDown extends React.Component {
  constructor(props) {
    super(props);
    this.change = this.change.bind(this)
  }

  change(event) {
    this.props.chooseUserFn(event.target.value)
  }

  render() {
    var users = this.props.users.map(user =>
        <UserOption key={user._links.self.href} user={user}/>
    );

    return (
        <div>
          <select onChange={this.change}>
            <option value="" selected>Choose user</option>
            {users}
          </select>
        </div>
    )
  }
}

class UserOption extends React.Component {
  render() {
    return <option value={this.props.user.id}>{this.props.user.name}:
      promotion={this.props.user.promotion}</option>
  }
}

class CheckoutList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      checkout: {}
    }
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange(event) {
  }

  handleSubmit(event) {
    event.preventDefault();

    axios.post(`/api/checkout`, this.props.checkout)
    .then(res => {
      this.setState({
        checkout: res.data
      })
    })
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
            <button type="submit">Add new checkout</button>
          </form>
          <pre>{JSON.stringify(this.state.checkout, null, 2)}</pre>
        </div>
    )
  }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)

