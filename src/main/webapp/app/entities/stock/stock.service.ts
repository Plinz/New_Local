import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStock } from 'app/shared/model/stock.model';
import { IUser } from '../../core/user/user.model';
import { HttpParams } from '@angular/common/http';

type EntityResponseType = HttpResponse<IStock>;
type EntityArrayResponseType = HttpResponse<IStock[]>;

@Injectable({ providedIn: 'root' })
export class StockService {
    public resourceUrl = SERVER_API_URL + 'api/stocks';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/stocks';

    constructor(private http: HttpClient) {}

    create(stock: IStock): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(stock);
        return this.http
            .post<IStock>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(stock: IStock): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(stock);
        return this.http
            .put<IStock>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IStock>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStock[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStock[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(stock: IStock): IStock {
        const copy: IStock = Object.assign({}, stock, {
            onSaleDate: stock.onSaleDate != null && stock.onSaleDate.isValid() ? stock.onSaleDate.toJSON() : null,
            expiryDate: stock.expiryDate != null && stock.expiryDate.isValid() ? stock.expiryDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.onSaleDate = res.body.onSaleDate != null ? moment(res.body.onSaleDate) : null;
        res.body.expiryDate = res.body.expiryDate != null ? moment(res.body.expiryDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((stock: IStock) => {
            stock.onSaleDate = stock.onSaleDate != null ? moment(stock.onSaleDate) : null;
            stock.expiryDate = stock.expiryDate != null ? moment(stock.expiryDate) : null;
        });
        return res;
    }

    findProduitBio(): Observable<EntityResponseType> {
        return this.http.get<IStock>(`${this.resourceUrl}/bio`, { observe: 'response' });
    }

    findNewProduit(): Observable<EntityResponseType> {
        return this.http.get<IStock>(`${this.resourceUrl}/newStock`, { observe: 'response' });
    }

    findBestPurchase(): Observable<EntityResponseType> {
        return this.http.get<IStock>(`${this.resourceUrl}/bestPurchase`, { observe: 'response' });
    }

    findGrade(): Observable<EntityResponseType> {
        return this.http.get<IStock>(`${this.resourceUrl}/grade`, { observe: 'response' });
    }

    findBySellerIsCurrentUser(): Observable<EntityArrayResponseType> {
        return this.http
            .get<IStock[]>(`${this.resourceUrl}/currentuser`, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getStockCat(name: string): Observable<EntityArrayResponseType> {
        return this.http
            .get<IStock[]>(`${this.resourceUrl}/category/${name}`, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    allSeller(): Observable<EntityArrayResponseType> {
        return this.http.get<IUser[]>(`${this.resourceUrl}/allseller`, { observe: 'response' });
    }

    filter(params: HttpParams): Observable<EntityArrayResponseType> {
        return this.http
            .get<IStock[]>(this.resourceUrl, { params, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }
}
