import requests

def print_dict(dict):
    print(str(dict)
    .replace(', ', ',\n\t')
    .replace('{', '{\n\t')
    .replace('}', '\n}')
    .replace('\'', '')
    .replace('\t{', '{')
    )    
