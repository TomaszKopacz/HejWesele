import sys
import os
import requests

var_name = sys.argv[1]
gitlab_api_url = os.environ["CI_API_V4_URL"]
project_id = os.environ["CI_PROJECT_ID"]
access_token = os.environ["GITLAB_ACCESS_TOKEN"]

def increment_build_number():
    build_number_response = get_build_number_response()
    if build_number_response.status_code == 404:
        return create_build_number()
    else:
        build_number = build_number_response.json()["value"]
        build_number_int = int(build_number)
        build_number_int += 1
        build_number = str(build_number_int)
        return update_build_number(build_number)

def get_build_number_response():
    headers = {"PRIVATE-TOKEN": access_token}
    endpoint = f"{gitlab_api_url}/projects/{project_id}/variables/{var_name}"
    response = requests.get(endpoint, headers=headers)
    return response

def create_build_number():
    headers = {"PRIVATE-TOKEN": access_token}
    endpoint = f"{gitlab_api_url}/projects/{project_id}/variables"
    body = {
        "key": var_name,
        "value": "1"
    }
    response = requests.post(endpoint, headers = headers, data=body).json()
    return response

def update_build_number(value):
    headers = {"PRIVATE-TOKEN": access_token}
    endpoint = f"{gitlab_api_url}/projects/{project_id}/variables/{var_name}"
    body = {
        "key": var_name,
        "value": value
    }
    response = requests.put(endpoint, headers=headers, data=body).json()
    return response

print(increment_build_number()["value"])
