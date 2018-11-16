/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseComponent } from 'app/entities/purchase/purchase.component';
import { PurchaseService } from 'app/entities/purchase/purchase.service';
import { Purchase } from 'app/shared/model/purchase.model';

describe('Component Tests', () => {
    describe('Purchase Management Component', () => {
        let comp: PurchaseComponent;
        let fixture: ComponentFixture<PurchaseComponent>;
        let service: PurchaseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseComponent],
                providers: []
            })
                .overrideTemplate(PurchaseComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PurchaseComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Purchase(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.purchases[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
