import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPurchaseDone } from 'app/shared/model/purchase-done.model';

type EntityResponseType = HttpResponse<IPurchaseDone>;
type EntityArrayResponseType = HttpResponse<IPurchaseDone[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseDoneService {
    public resourceUrl = SERVER_API_URL + 'api/purchase-dones';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/purchase-dones';

    constructor(private http: HttpClient) {}

    create(purchaseDone: IPurchaseDone): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(purchaseDone);
        return this.http
            .post<IPurchaseDone>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(purchaseDone: IPurchaseDone): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(purchaseDone);
        return this.http
            .put<IPurchaseDone>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPurchaseDone>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPurchaseDone[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPurchaseDone[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(purchaseDone: IPurchaseDone): IPurchaseDone {
        const copy: IPurchaseDone = Object.assign({}, purchaseDone, {
            saleDate: purchaseDone.saleDate != null && purchaseDone.saleDate.isValid() ? purchaseDone.saleDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.saleDate = res.body.saleDate != null ? moment(res.body.saleDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((purchaseDone: IPurchaseDone) => {
            purchaseDone.saleDate = purchaseDone.saleDate != null ? moment(purchaseDone.saleDate) : null;
        });
        return res;
    }
}
