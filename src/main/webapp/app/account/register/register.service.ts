import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class Register {
    constructor(private http: HttpClient) {}

    save(account: any, seller: boolean): Observable<any> {
        if (seller) {
            account.authorities = 'ROLE_SELLER';
        }
        return this.http.post(SERVER_API_URL + 'api/register', account);
    }
}
