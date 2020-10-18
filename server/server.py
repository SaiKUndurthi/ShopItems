#https://blog.miguelgrinberg.com/post/designing-a-restful-api-with-python-and-flask

from flask import Flask, jsonify, request, make_response, abort
import os
from scipy import misc

app = Flask(__name__)

@app.errorhandler(400)
def bad_request(error):
    return make_response(jsonify({'error': 'bad request'}), 400)

@app.errorhandler(401)
def invalid(error):
    return make_response(jsonify({'error': 'invalid'}), 401)

shoplist = [
	{
		'name': 'Jellyfish',
		'image': 'jellyfish',
		'description': "It stings",
        'date': "07/30/2029",
		'cost': 1
	},
	{
		'name': 'Kitten',
		'image': 'kitten',
		'description': "It purrs",
        'date': "07/30/2026",
		'cost': 1
	}
]

@app.route('/list', methods=['GET'])
def get_list():
    print("list")
    return jsonify({'list': shoplist})

@app.route('/register', methods=['POST'])
def post_register():
    print("user details")

    if not request.json:
        abort(400)

    if 'email' in request.json:
        print("User details--------> Frist name: " + request.json.get('firstname', '?') + " Last name: "+request.json.get('lastname', '?')+" @Email: "+ request.json.get('email','?'))
    else:
        print("?")

    return jsonify({'user': 0})

@app.route('/login', methods=['POST'])
def post_login():
    print("user details")

    if not request.json:
        abort(400)

    if 'email' in request.json:
        print("Login User details--------> "+" @Email: "+ request.json.get('email','?'))
    else:
        print("?")

    return jsonify({'user': 0})

@app.route('/buy', methods=['POST'])
def post_buy():
    print("buy")

    if not request.json:
        abort(400)

    if 'name' in request.json:
        print("Bought: " + request.json.get('name', '?') +" @ $"+ str(request.json.get('cost','?')))
    else:
        print("?")

    return jsonify({'spend': 0})

app.run(host= '10.0.0.71',port='5005',debug=True)
