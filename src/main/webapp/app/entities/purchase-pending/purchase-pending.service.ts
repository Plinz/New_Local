import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPurchasePending } from 'app/shared/model/purchase-pending.model';

type EntityResponseType = HttpResponse<IPurchasePending>;
type EntityArrayResponseType = HttpResponse<IPurchasePending[]>;

@Injectable({ providedIn: 'root' })
export class PurchasePendingService {
    public resourceUrl = SERVER_API_URL + 'api/purchase-pendings';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/purchase-pendings';

    constructor(private http: HttpClient) {}

    create(purchasePending: IPurchasePending): Observable<EntityResponseType> {
        return this.http.post<IPurchasePending>(this.resourceUrl, purchasePending, { observe: 'response' });
    }

    update(purchasePending: IPurchasePending): Observable<EntityResponseType> {
        return this.http.put<IPurchasePending>(this.resourceUrl, purchasePending, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPurchasePending>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPurchasePending[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPurchasePending[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
