import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NavbarService {
    count: number;
    subject = new Subject<number>();

    constructor() {
        this.count = 0;
    }

    sendIncrement() {
        this.count++;
        this.subject.next(this.count);
    }

    sendDecrement() {
        this.count--;
        this.subject.next(this.count);
    }

    clearCount() {
        this.count = 0;
        this.subject.next(this.count);
    }

    getCount(): Observable<number> {
        return this.subject.asObservable();
    }
}
